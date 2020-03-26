package com.mx.independencia.pymefectivo.contactsservice.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.mx.independencia.pymefectivo.contactsservice.configuration.DBParameters;
import com.mx.independencia.pymefectivo.contactsservice.models.Contact;

@Repository
public class ContactRepositoryImplementation implements ContactRepository {
	
	private static final Logger log = LoggerFactory.getLogger(ContactRepositoryImplementation.class);
	private DataSource ds;
	
	@Autowired
	private DBParameters db;
	
	private void configureDataSource() {
		Properties props = new Properties();
		props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
		props.setProperty("dataSource.user", db.getUser());
		props.setProperty("dataSource.password", db.getPass());
		props.setProperty("dataSource.databaseName", db.getName());
		
		HikariConfig config = new HikariConfig(props);
		ds = new HikariDataSource(config);
	}
	


	@Override
	public String create(Contact element) {
		String response = "";

		configureDataSource();
		try (Connection con = ds.getConnection()) {
			try (PreparedStatement statement = con
					.prepareStatement("INSERT INTO contact_fase_0 (name, email, phone, topic, message) VALUES (?, ?, ?, ?, ?)")) {
				statement.setString(1, element.getName());
				statement.setString(2, element.getEmail());
				statement.setString(3, element.getPhone());
				statement.setString(4, element.getTopic());
				statement.setString(5, element.getMessage());
				int resp = statement.executeUpdate();
				if (resp != 0 ) {
					response = "ok";
				}
			}
		} catch (SQLException e) {
			log.error("Algo falló en la consulta: {}", e.getMessage());
			response = e.getMessage();
		}
		return response;
	}
}
