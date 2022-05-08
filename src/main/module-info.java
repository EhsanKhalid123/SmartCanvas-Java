module A2.Smart.Canvas.Ehsan.Khalid {

    requires javafx.graphics;
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires java.sql;
    requires javafx.swt;

    opens controller;
    opens model;
    opens resources.views;
    opens dao;

}