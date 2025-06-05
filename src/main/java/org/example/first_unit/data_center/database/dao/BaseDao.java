package org.example.first_unit.data_center.database.dao;
import java.sql.ResultSet;

public interface BaseDao<E> {
    public abstract E insert(E e);
    public boolean delete(E e);
    public boolean update(E e);
    public E findById(E e);
    public ResultSet findAll();
    public Integer getRegistersQuantity();
}
