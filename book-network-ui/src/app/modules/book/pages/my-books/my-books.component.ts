import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {Router} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";
import {BookResponse} from "../../../../services/models/book-response";

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent  implements OnInit{

  page = 0;
  size = 5;
  bookResponse: PageResponseBookResponse = {};


  constructor(private router: Router, private bookService: BookService) {
  }

  ngOnInit() {
    this.findAllBooks();
  }

  private findAllBooks(){
    this.bookService.findAllBooksByOwner({
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

  archiveBook(book: BookResponse) {
    this.bookService.updateArchivedStatus({
      "book-id": book.id as number,
    }).subscribe({
      next: () => {
        book.archived = !book.archived;
      }
    })
  }

  editBook(book: BookResponse) {
      this.router.navigate(['books', 'manage', book.id])
  }

  shareBook(book: BookResponse) {
      this.bookService.updateShareableStatus({
        "book-id": book.id as number,
      }).subscribe({
        next: () => {
          book.shareable = !book.shareable;
        }
      })
  }
}
