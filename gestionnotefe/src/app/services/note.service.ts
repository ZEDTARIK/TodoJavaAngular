import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { CustomHttpResponse } from '../interface/custom-http-response';
import { Note } from '../interface/note';

@Injectable({
  providedIn: 'root'
})
export class NoteService {

  private readonly server= 'http://localhost:8085';

  constructor(private http: HttpClient) { }

  notes$ = <Observable<CustomHttpResponse>>this.http.get<CustomHttpResponse>
    (`${this.server}/note/all`)
    .pipe(
      tap(console.log),
      catchError(this.handlErrors)
    );

  save$ = (note: Note) => <Observable<CustomHttpResponse>>this.http.post<CustomHttpResponse>
    (`${this.server}/note/add`, note)
    .pipe(
      tap(console.log),
      catchError(this.handlErrors)
    );

  update$ = (note: Note) => <Observable<CustomHttpResponse>>this.http.put<CustomHttpResponse>
    (`${this.server}/note/update`, note)
    .pipe(
      tap(console.log),
      catchError(this.handlErrors)
    );

  delete$ = (noteId: number) => <Observable<CustomHttpResponse>>this.http.delete<CustomHttpResponse>
    (`${this.server}/note/delete/${noteId}`)
    .pipe(
      tap(console.log),
      catchError(this.handlErrors)
    );

  private handlErrors(error: HttpErrorResponse): Observable<never> {
    console.error('Error caught in handleErrors:', error);

    let errorMessage: string;
    // Client error
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client error occured - ${error.error.message}`;
    } else {
      errorMessage = `Server returned code ${error.status}`;
      // Server error
      if (error.error) {
        errorMessage += `, body was: ${JSON.stringify(error.error)}`;
      } else {
        errorMessage += `, response body is empty or unavailable.`;
      }
    }
    return throwError(errorMessage);
  }


}
