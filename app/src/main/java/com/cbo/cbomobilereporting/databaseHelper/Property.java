package com.cbo.cbomobilereporting.databaseHelper;

/**
 * Created by cboios on 24/01/19.
 */

public abstract class Property<T>  {
    private T value;
    public T get(){
        return value;
    }
    public Property<T> set(T value){
        this.value = value;
       return this;
    }
}
