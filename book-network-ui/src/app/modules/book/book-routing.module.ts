import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from "./pages/main/main.component";
import {BookListComponent} from "./pages/book-list/book-list.component";
import {MyBooksComponent} from "./pages/my-books/my-books.component";
import {ManageBookComponent} from "./pages/manage-book/manage-book.component";
import {BorrowedBooksComponent} from "./pages/borrowed-books/borrowed-books.component";
import {ReturnedBooksComponent} from "./pages/returned-books/returned-books.component";
import {authGuard} from "../../services/guard/auth.guard";

const routes: Routes = [
  {
    path: "",
    component: MainComponent,
    canActivate: [authGuard],
    children: [
      {
        path: "",
        component: BookListComponent
      }
    ]
  },
  {
    path: "my-books",
    canActivate: [authGuard],
    component: MyBooksComponent
  },
  {
    path: "manage",
    canActivate: [authGuard],
    component: ManageBookComponent
  },
  {
    path: "manage/:bookId",
    canActivate: [authGuard],
    component: ManageBookComponent
  },
  {
    path: "my-borrowed-books",
    canActivate: [authGuard],
    component: BorrowedBooksComponent
  },
  {
    path: "my-returned-books",
    canActivate: [authGuard],
    component: ReturnedBooksComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
