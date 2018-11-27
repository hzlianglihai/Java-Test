package com.netease.haitao.crm.service.birthday.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.PeekingIterator;
import com.google.common.io.Files;
import com.neteas.common.collect.Iterators;
import com.netease.haitao.crm.util.UUIDUtils;

public class FileSort {
	static File[] file = new File[100];
	static OutputStreamWriter[] outputStreamWriter = new OutputStreamWriter[100];
	static File newFile = null;
	static HashMap<String, Long> wordsMap = new HashMap<>();
	// 原始大文件中的行数;
	static int count = 0;
	static int countNew = 0;

	public static void main(String[] args) throws IOException {
		// 原始大文件
		File oldInput = File.createTempFile("file", ".txt");
		// 新的大文件
		newFile = File.createTempFile("newFile", ".txt");
		// 分割成100个小文件
		splitLargeFile(oldInput);
		// 读取100个小文件中的单词并统计个数，写入新的大文件newInput;
		parseSmallFile();
		// 归并排序新的大文件；
		mergeSort(newFile);
		// TODO 读取排好序的新的大文件中前100行;
	}

	public static void splitLargeFile(File input) throws IOException {
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		for (int i = 0; i < 100; i++) {
			// 创建100个文件
			file[i] = File.createTempFile("tmp-" + UUIDUtils.newId(), ".txt");
			outputStreamWriter[i] = new OutputStreamWriter(new FileOutputStream(file[i]));
		}
		// 读取大文件
		read = new InputStreamReader(new FileInputStream("file.txt"), "utf-8");
		bufferedReader = new BufferedReader(read, 5 * 1024 * 1024);// 设置缓存大小
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			count++;
			parseLineWords(line);
		}
		for (int i = 0; i < 100; i++) {
			if (outputStreamWriter[i] != null) {
				outputStreamWriter[i].close();
				outputStreamWriter[i] = null;
			}
		}
		if (bufferedReader != null) {
			bufferedReader.close();
			bufferedReader = null;
		}
		System.out.println("文件中行数为 ： " + count);

	}

	private static void parseLineWords(String line) throws IOException {
		String[] words = line.split(" ");
		for (int i = 0; i < words.length; i++) {
			int index = words[i].hashCode() % 1000;
			outputStreamWriter[index].write(words[i] + " ");
			outputStreamWriter[index].flush();
		}
	}

	private static void parseSmallFile() throws IOException {
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(read, 5 * 1024 * 1024);// 设置缓存大小
		String line = null;
		for (int i = 0; i < 100; i++) {
			if (file[i].isFile()) {
				read = new InputStreamReader(new FileInputStream(file[i]), "utf-8");
				while ((line = bufferedReader.readLine()) != null) {
					parseEachWords(line);
				}
				// TODO 对每个小文件对应的HashMap顺序取出每个单词和计数写入另外1个大文件(每个一行)；
				wordsMap.clear();
			}
		}
	}

	private static File split(File input, long from, long to) throws IOException {
		File part = File.createTempFile("sort", ".txt");
		long lineNumber = 0L;
		String line = null;
		try (Stream<String> stream = Files.newReader(input, Charsets.UTF_8).lines();
				Writer writer = Files.newWriter(part, Charsets.UTF_8)) {
			Iterator<String> lineIterator = stream.iterator();
			while (lineIterator.hasNext() && lineNumber <= to) {
				line = lineIterator.next();
				if (lineNumber >= from) {
					writer.write(line.concat(IOUtils.LINE_SEPARATOR));
				}
				lineNumber++;
			}
		}
		return part;
	}

	private static File inMemorySort(File input) throws IOException {
		TreeSet<String> lines = new TreeSet<>();
		InputStreamReader read = new InputStreamReader(new FileInputStream(input), "utf-8");
		BufferedReader bufferedReader = new BufferedReader(read, 5 * 1024 * 1024);// 设置缓存大小
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		FileUtils.writeLines(input, lines);
		return input;
	}

	// 对上面新生成的大文件进行归并排序；
	private static File mergeSort(File newInput) throws IOException {
		long total = countNew;
		if (total <= 200) {
			return inMemorySort(newInput);
		}
		long half = total / 2L;
		File left = mergeSort(split(newInput, 0, half));
		File right = mergeSort(split(newInput, half + 1, total));
		return merge(newInput, left, right);
	}

	private static File merge(File source, File left, File right) throws IOException {
		try (Stream<String> leftStream = Files.newReader(left, Charsets.UTF_8).lines();
				Stream<String> rightStream = Files.newReader(right, Charsets.UTF_8).lines();
				Writer writer = Files.newWriter(source, Charsets.UTF_8)) {
			com.neteas.common.collect.PeekingIterator<String> leftPeekingIterator = Iterators
					.peekingIterator(leftStream.iterator());
			com.neteas.common.collect.PeekingIterator<String> rightPeekingIterator = Iterators
					.peekingIterator(rightStream.iterator());
			String leftLine, rightLine;
			writer.write("");
			while (leftPeekingIterator.hasNext() && rightPeekingIterator.hasNext()) {
				leftLine = leftPeekingIterator.peek();
				rightLine = rightPeekingIterator.peek();
				if (leftLine.compareTo(rightLine) > 0) {
					writer.append(leftLine.concat(IOUtils.LINE_SEPARATOR));
					leftPeekingIterator.next();
				} else {
					writer.append(rightLine.concat(IOUtils.LINE_SEPARATOR));
					rightPeekingIterator.next();
				}
			}
			while (leftPeekingIterator.hasNext()) {
				writer.append(leftPeekingIterator.next().concat(IOUtils.LINE_SEPARATOR));
			}
			while (rightPeekingIterator.hasNext()) {
				writer.append(rightPeekingIterator.next().concat(IOUtils.LINE_SEPARATOR));
			}
		}
		return source;
	}

	private static void parseEachWords(String line) {
		String[] words = line.split(" ");
		for (int i = 0; i < words.length; i++) {
			if (wordsMap.containsKey(words[i])) {
				wordsMap.put(words[i], wordsMap.get(words[i] + 1));
			} else {
				wordsMap.put(words[i], 1L);
			}
		}
	}

}
