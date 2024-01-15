
import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { startWith, catchError, map} from 'rxjs/operators';
import { NoteService } from './services/note.service';
import { DataState } from './enumaration/data-state';
import { AppState } from './interface/app-state';
import { CustomHttpResponse } from './interface/custom-http-response';
import { Level } from './enumaration/level';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  appState$: Observable<AppState<CustomHttpResponse>> | undefined;
  readonly DataState = DataState;
  readonly Level= Level;

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

}
