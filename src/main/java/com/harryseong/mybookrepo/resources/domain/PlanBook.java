package com.harryseong.mybookrepo.resources.domain;

import javax.persistence.*;

@Entity
@Table(name = "plan_book")
public class PlanBook {

    @EmbeddedId
    private PlanBook_PK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("planId")
    private Plan plan;

    private Integer status;

    public PlanBook() { }

    public PlanBook(Book book, Plan plan) {
        this.book = book;
        this.plan = plan;
        this.id = new PlanBook_PK(plan.getId(), book.getId());
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public PlanBook_PK getId() {
        return id;
    }

    public void setId(PlanBook_PK id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
