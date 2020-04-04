package models;

import java.time.LocalDateTime;

public class CarModelEntity {

    private Long id;
    private String name;
    private LocalDateTime startYear;
    private LocalDateTime endYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartYear() {
        return startYear;
    }

    public void setStartYear(LocalDateTime startYear) {
        this.startYear = startYear;
    }

    public LocalDateTime getEndYear() {
        return endYear;
    }

    public void setEndYear(LocalDateTime endYear) {
        this.endYear = endYear;
    }
}
