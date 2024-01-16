import { Note } from './interface/note';

import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { startWith, catchError, map} from 'rxjs/operators';
import { NoteService } from './services/note.service';
import { DataState } from './enumaration/data-state';
import { AppState } from './interface/app-state';
import { CustomHttpResponse } from './interface/custom-http-response';
import { Level } from './enumaration/level';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  appState$: Observable<AppState<CustomHttpResponse>> | undefined;
  readonly DataState = DataState;
  readonly Level= Level;
  private dataSubject = new BehaviorSubject<CustomHttpResponse | undefined>(undefined);

  constructor(private noteService: NoteService) { }

  ngOnInit(): void {
    this.appState$ = this.noteService.notes$
      .pipe(
        map(response => {
          return { dataState: DataState.LOADED, data: response }
        }),
        startWith({ dataState: DataState.LOADING }),
        catchError((error: string) => {
          return of({ dataState: DataState.ERROR, error: error })
        })
      );
  }


  saveNote(noteForm: NgForm): void {
    this.appState$ = this.noteService.save$(noteForm.value)
      .pipe(
        map(response => {

          this.dataSubject.next(<CustomHttpResponse>{
              ...this.dataSubject.value,
              notes: [response.notes![0], ...this.dataSubject.value!.notes!]
            });

            noteForm.reset({title: '', description: '', level: this.Level.HIGH});

          return { dataState: DataState.LOADED, data: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED, data: this.dataSubject.value }),
        catchError((error: string) => {
          return of({ dataState: DataState.ERROR, error: error })
        })
      );
  }


}
