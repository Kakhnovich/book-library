package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.model.ReaderFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ReaderServiceImplTest {
    List<Reader> readerList;
    ReaderServiceImpl readerService;
    HashMap<String, String> hashMap = new HashMap<>();
    @Mock
    ReaderDao readerDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        readerService = ReaderServiceImpl.getInstance();
        readerService.setReaderDao(readerDao);
        readerList = new ArrayList<>(Arrays.asList(
                ReaderFactory.getInstance().create(1,"email1", "firstName1", "lastName1", "male", 0),
                ReaderFactory.getInstance().create(2,"email2", "firstName2", "lastName2", "male", 0),
                ReaderFactory.getInstance().create(3,"email3", "firstName3", "lastName3", "female", 0)));
        for (Reader reader : readerList) {
            hashMap.put(reader.getEmail(), reader.getFirstName() + ' ' + reader.getLastName());
        }
    }

    @Test
    public void testFindAll_ShouldReturnListOfAllReaders() {
        when(readerDao.findAll()).thenReturn(Optional.of(readerList));
        List<Reader> actual = readerService.findAll();
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of readers: " + readerList.size(), readerList.size(), actual.size());
        for (int i = 0; i < readerList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", readerList.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByPage_ShouldReturnListOfAllReaders() {
        when(readerDao.findByPageNumber(1)).thenReturn(Optional.of(readerList));
        List<Reader> actual = readerService.findByPage(1);
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals("Expected count of readers: " + readerList.size(), readerList.size(), actual.size());
        for (int i = 0; i < readerList.size(); i++) {
            assertEquals((i + 1) + " element expected should be correct", readerList.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByKey_ShouldReturnFirstReader() {
        when(readerDao.findByKey(readerList.get(0).getId())).thenReturn(Optional.of(readerList.get(0)));
        Reader actual = readerService.findByKey(readerList.get(0).getId());
        assertEquals(readerList.get(0), actual);
    }

    @Test
    public void testFindReaderByEmail_ShouldReturnFirstReader() {
        when(readerDao.findAll()).thenReturn(Optional.of(readerList));
        Reader actual = readerService.findReaderByEmail(readerList.get(0).getEmail());
        assertEquals(readerList.get(0), actual);
    }

    @Test
    public void testGetCountOfPages_ShouldReturnOne() {
        when(readerDao.getCountOfPages()).thenReturn(1);
        int actual = readerService.getCountOfPages();
        assertEquals(1, actual);
    }

    @Test
    public void testCreate_ShouldReturnSecondReader() {
        when(readerDao.create(readerList.get(1))).thenReturn(Optional.of(readerList.get(1)));
        when(readerDao.findByEmail(readerList.get(1).getEmail())).thenReturn(Optional.of(readerList.get(1)));
        Reader actual = readerService.create(readerList.get(1));
        assertEquals(readerList.get(1), actual);
    }

    @Test
    public void testUpdate_ShouldReturnThirdReader() {
        when(readerDao.update(readerList.get(2))).thenReturn(Optional.of(readerList.get(2)));
        Reader actual = readerService.update(readerList.get(2));
        assertEquals(readerList.get(2), actual);
    }

    @Test
    public void testFindEmailsWithNames_ShouldReturnMapOfReadersEmailsAndNames() {
        when(readerDao.findAll()).thenReturn(Optional.of(readerList));
        HashMap<String, String> actual = readerService.findEmailsWithNames();
        assertFalse("Result shouldn't be empty", actual.isEmpty());
        assertEquals(hashMap.size(), actual.size());
        for (String key : hashMap.keySet()) {
            assertTrue("Map should contains " + key + " key", actual.containsKey(key));
            assertEquals(key + " map value should be correct", hashMap.get(key), actual.get(key));
        }
    }
}