package ru.otus;

import ru.otus.annotations.*;

public class TestClass {
    @Before
    public void before1() {
        System.out.print("Before_1 \n");
    }

    @Before
    public void before2() {
        System.out.print("Before_2 \n");
    }

    @Test
    public void test() {
        System.out.print("test \n");
    }

    @Test
    public void testException() throws Exception {
        throw new Exception();
    }

    @After
    public void  after1() {
        System.out.print("After_1 \n");
    }

    @After
    public void after2() {
        System.out.print("After_2 \n");
    }
}
