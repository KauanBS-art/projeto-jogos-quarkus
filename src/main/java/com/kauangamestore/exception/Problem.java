//Classe de Kauan Batista Silveira

package com.kauangamestore.exception;

import java.time.LocalDateTime;
import java.util.List;

public class Problem {

    private Integer status;
    private String type;
    private String title;
    private String detail;
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<String> violations;

    public Problem() {}

    public Problem(Integer status, String type, String title, String detail) {
        this.status = status;
        this.type = type;
        this.title = title;
        this.detail = detail;
    }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<String> getViolations() { return violations; }
    public void setViolations(List<String> violations) { this.violations = violations; }
}