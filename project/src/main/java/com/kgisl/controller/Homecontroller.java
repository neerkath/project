package com.kgisl.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.kgisl.model.City;

@WebServlet("/home")
public class Homecontroller  extends HttpServlet{
    private JdbcTemplate jdbcTemplate;
    static ArrayList<City> cityList = new ArrayList<City>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException, FileNotFoundException {
        String fileName = "worldcities1.csv";
        InputStream stream = this.getClass().getResourceAsStream(fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
         { String line;
            while ((line = br.readLine()) != null)
             {
                String part[] =line.split(",");
                City city = new City(part[0], part[1], part[2], part[3],part[4], part[5], part[6], part[7],part[8], part[9], part[10]);
                cityList.add(city);
             }}
              cityList.forEach(System.out::println);
              insertBatch(cityList);
             }
             public void insertBatch(final List<City> citys){
                 String sql = "INSERT INTO CITY " + "(city,city_ascii,lat,lng,country,iso2,iso3,admin_name,capital,population,id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                 DriverManagerDataSource dataSource = new DriverManagerDataSource();
                 dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                  dataSource.setUrl("jdbc:mysql://localhost:3306/neerkath");
                  dataSource.setUsername("root");
                   dataSource.setPassword("");
                     jdbcTemplate=new JdbcTemplate(dataSource);
                     jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                        @Override
                         public void setValues(PreparedStatement ps, int i) throws SQLException {
                             City city = citys.get(i);
                              ps.setString(1, city.getCity());
                              ps.setString(2, city.getCity_ascii());
                              ps.setString(3, city.getLat() );
                              ps.setString(4, city.getLng());
                               ps.setString(5, city.getCountry());
                                ps.setString(6, city.getIso2());
                                 ps.setString(7, city.getIso3());
                                  ps.setString(8, city.getAdmin_name()); 
                                  ps.setString(9, city.getCapital());
                                  ps.setString(10, city.getPopulation());
                                   ps.setString(11, city.getId());
                                }
                                 @Override
                                 public int getBatchSize() {
                                     return citys.size();
                                    }
                                });         
                                }
}
