package com.harryseong.books.dto;

public class BookDTO {
    private String title;
    private String publisher;
    private Integer publishedDate;
    private String description;
    private Integer pageCount;
    private String coverImageURL;
    private String printType;
    private String isbn10;
    private String isbn13;
    private String otherIdType;
    private AuthorDTO[] authors;
    private CategoryDTO[] categories;

    public BookDTO() {
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

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public AuthorDTO[] getAuthors() {
        return authors;
    }

    public void setAuthors(AuthorDTO[] authors) {
        this.authors = authors;
    }

    public CategoryDTO[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryDTO[] categories) {
        this.categories = categories;
    }
}
