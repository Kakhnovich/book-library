package com.itechart.studets_lab.book_library.service.parser;

import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.service.ReaderService;
import com.itechart.studets_lab.book_library.service.impl.ReaderServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BorrowParser {
    private static final BorrowParser INSTANCE = new BorrowParser();
    private static final String VARIABLES_SPLIT_REGEX = ";";
    private static final String BORROWS_SPLIT_REGEX = ",";
    private final ReaderService readerService = ReaderServiceImpl.getInstance();

    BorrowParser() {
    }

    public static BorrowParser getInstance() {
        return INSTANCE;
    }

    public List<BorrowDto> parseString(String inputString) {
        List<BorrowDto> borrows = new ArrayList<>();
        for (String borrowData : inputString.split(BORROWS_SPLIT_REGEX)) {
            borrows.add(parseBorrow(borrowData));
        }
        return borrows;
    }

    private BorrowDto parseBorrow(String borrowData) {
        List<String> dataList = Arrays.asList(borrowData.split(VARIABLES_SPLIT_REGEX).clone());
        String firstName = StringUtils.substringBefore(dataList.get(3), " ");
        String lastName = StringUtils.substringAfter(dataList.get(3), " ");
        Reader reader = readerService.findReaderByEmail(dataList.get(2));
        reader.setFirstName(firstName);
        reader.setLastName(lastName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new BorrowDto(Integer.parseInt(dataList.get(0)), Integer.parseInt(dataList.get(1)), reader, LocalDate.parse(dataList.get(4), formatter),
                Integer.parseInt(dataList.get(5)), dataList.get(6).equals("") ? LocalDate.now() : LocalDate.parse(dataList.get(6), formatter), dataList.get(7), dataList.get(8));
    }
}
