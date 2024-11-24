package dao;

import java.sql.SQLException;
import java.util.List;

import models.Notification;

public interface InterfaceNotificationDAO extends DAO<Notification> {
	List<Notification> getNotificationsByStatus(String status, int recipientID) throws SQLException;

}
