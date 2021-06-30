// Test.aidl
package com.example.testdemo.aidl;

// Declare any non-default types here with import statements
import com.example.testdemo.aidl.TestBean;

interface Test {
    void testAidl(int aInt);
    void addTestBean(in TestBean bean);
    List<TestBean> getBean();
}