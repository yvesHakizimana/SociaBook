import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent  implements OnInit{
  page = 0;
  size = 5;
  bookResponse: PageResponseBookResponse = {};


  constructor(private router: Router, private bookService: BookService) {
  }

  ngOnInit() {
    this.findAllBooks();
  }

  private findAllBooks(){
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe(({
      next: (books) => {
        this.bookResponse = books;
      },
      error: (err) => {
        console.log(err);
      }
    }))
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  goToPage(index: number) {
    this.page = index;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page =  this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  get isLastPage(){
    return this.page == this.bookResponse.totalPages as number - 1;
  }
}
