package com.harryseong.mybookrepo.resources.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlanBook_PK implements Serializable {

    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "book_id")
    private Integer bookId;

    public PlanBook_PK() {
    }

    public PlanBook_PK(Integer planId, Integer bookId) {
        this.planId = planId;
        this.bookId = bookId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanBook_PK that = (PlanBook_PK) o;
        return planId.equals(that.planId) &&
            bookId.equals(that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planId, bookId);
    }
}
