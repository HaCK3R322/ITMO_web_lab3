package com.androsov.ITMO_web_lab3;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;

class Result {
    public Result(float x, float y, float r, boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
    }

    public float x;
    public float y;
    public float r;
    public boolean hit;
}

@Named
@ApplicationScoped
public class ResultsCollection {
    private boolean dbreaded = false;

    private LinkedList<Result> results = new LinkedList<>();

    public LinkedList<Result> getResults() {
        return results;
    }

    public void setResults(LinkedList<Result> results) {
        this.results = results;
    }

    //JNDI of datasource on wildfly
    @Resource(lookup="java:/PostgresDS")
    private DataSource ds;

    private Connection connection = null;
    private Connection getConnection() {
        if(connection == null) {
            try {
                connection = ds.getConnection();
            } catch (Exception e) {
                throw new Error("CANNOT CONNECT TO DATASOURCE:\n" + e.getMessage());
            }
        }
        return connection;
    }

    //TODO: change to normal database reading
    public void readFromDatabase() {
        if(dbreaded) return;

        try {
            try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM results.res")) {
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(new Result(resultSet.getFloat("x"),
                                resultSet.getFloat("y"),
                                resultSet.getFloat("r"),
                                resultSet.getBoolean("hit")));
                    }
                }
            }
        } catch (SQLException ignored) {
            throw new Error("gg");
        }

        dbreaded = true;
    }

    public String getResultsTable() {
        String tableHTML = "";

        Collections.reverse(results);

        for (Result result : results) {
            String bgcolor = "red";
            if(result.hit) bgcolor="lightgreen";
            tableHTML += "<tr>" +
                    "<td>" + result.x + "</td>" +
                    "<td>" + result.y + "</td>" +
                    "<td>" + result.r + "</td>" +
                    "<td style=\"background-color:"+bgcolor+";\">" + result.hit + "</td>" +
                    "</tr>";
        }

        Collections.reverse(results);

//        tableHTML = "" + results.size();
        return tableHTML;
    }

    public String validateAndCheck(float x, float y, float r) {
        if(!(-3 <= y && y <= 5)) {
            showError("Y must be in bounds [-3, 5]!");
            return "mainpage";
        }

        if(!(-2 <= r && r <= 5)) {
            showError("R must be in bounds [-2, 5]!");
            return "mainpage";
        }

        if(r < 0) {
            showWarn("R cannot be below zero!");
            return "mainpage";
        }

        if(y > 1.25*r || y < -1.25*r)
            showWarn("Y out of coordinate space");

        if(x > 1.25*r || x < -1.25*r)
            showWarn("X out of coordinate space");


        boolean hit = false;
        if( ((x*x + y*y <= r*r) && (-r <= x && x <= 0) && (0 <= y && y <= r))
                ||
                ((-r <= x && x <= 0) && (-r/2 <= y && y <= 0))
                ||
                ((0 <= x && x <= r) && (y <= x/-2 + r/2) && (y >= 0)) )
        {
            hit = true;
        }

        Result result = new Result(x, y, r, hit);
        results.add(result);

        try {
            try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO results.res VALUES (?,?,?,?)")) {
                stmt.setFloat(1, x);
                stmt.setFloat(2, y);
                stmt.setFloat(3, r);
                stmt.setBoolean(4, hit);
                stmt.execute();
            }
        } catch (SQLException ignored) {
            throw new Error("gg");
        }

        return "mainpage";
    }

    public void showError(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, "Bounds error", message);
    }

    public void showWarn(String message) {
        addMessage(FacesMessage.SEVERITY_WARN, "Warning", message);
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String getPoints(float r) {
        String divsHTML = "";
        for(Result result: results) {
            float x_pxl = 0;
            float y_pxl = 0;
            String bgColor = "red";
            float x=result.x, y=result.y;

            divsHTML += "<div style=\"position: absolute;width: 7px;height: 7px;";
            divsHTML += "transform: translate(-50%, +50%);";



            if(y > 1.25*r || y < -1.25*r) {
                y_pxl=10000;
            } else {
                y_pxl = (float)((500 / 2.5) * (y / r) + 250);
            }


            if(x > 1.25*r || x < -1.25*r) {
                x_pxl=10000;
            } else {
                x_pxl = (float)((500 / 2.5) * (x / r) + 250);
            }

            if(result.hit)
                bgColor = "green";

            divsHTML += "background-color:" + bgColor + ";";

            divsHTML += "left:" + x_pxl + "px;";
            divsHTML += "bottom:" + y_pxl + "px;\"></div>";
        }

        return divsHTML;
    }
}
