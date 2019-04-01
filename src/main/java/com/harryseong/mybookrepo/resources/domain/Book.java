package com.harryseong.mybookrepo.resources.domain;

import com.harryseong.mybookrepo.resources.constraints.ISBN;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="book")
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull @NotBlank
    private String title;

    private String publisher;
    private Integer publishedDate;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Author> authors = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Category> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<UserBook> users = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<PlanBook> plans = new ArrayList<>();

    @Size(max=5000)
    private String description;

    private Integer pageCount;

    @URL
    @Column(name = "cover_image_url")
    private String coverImageURL;

    private String printType;

    @ISBN
    private String isbn13;

    @ISBN
    private String isbn10;

    private String otherIdType;

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

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Integer publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<UserBook> getUsers() {
        return users;
    }

    public void setUsers(List<UserBook> users) {
        this.users = users;
    }

    public List<PlanBook> getPlans() {
        return plans;
    }

    public void setPlans(List<PlanBook> plans) {
        this.plans = plans;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getOtherIdType() {
        return otherIdType;
    }

    public void setOtherIdType(String otherIdType) {
        this.otherIdType = otherIdType;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
