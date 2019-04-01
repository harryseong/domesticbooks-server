package com.harryseong.mybookrepo.resources.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "plan")
public class Plan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PlanBook> books = new ArrayList<>();

    private String name;
    private String description;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @ApiModelProperty(hidden=true)
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(nullable = false)
    @ApiModelProperty(hidden=true)
    private Date modifiedDate;

    public Plan() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PlanBook> getBooks() {
        return books;
    }

    public void setBooks(List<PlanBook> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        PlanBook planBook = new PlanBook(book, this);
        books.add(planBook);
        this.getBooks().add(planBook);
    }

    public void removeBook(Book book) {
        // Iterate through each UserBook "book".
        for (
            Iterator<PlanBook> iterator = books.iterator();
            iterator.hasNext();
        ) {
            PlanBook planBook = iterator.next();

            // If PlanBook is found to have plan to be removed:
            //   1. Remove the PlanBook from Plan and Book
            //   2. Set PlanBook's plan to null
            //   3. Set PlanBook's book to null
            //   Orphan removal will remove stranded PlanBook.
            if (planBook.getBook().equals(book)) {
                iterator.remove();
                planBook.getPlan().getBooks().remove(planBook);
                planBook.getBook().getPlans().remove(planBook);
                planBook.setPlan(null);
                planBook.setBook(null);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
