import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

import 'codemirror/mode/clike/clike';
import 'codemirror/mode/go/go';
import 'codemirror/mode/haskell/haskell';
import 'codemirror/mode/python/python';
import 'codemirror/mode/d/d';
import 'codemirror/mode/ruby/ruby';
import 'codemirror/mode/fortran/fortran';
import 'codemirror/mode/rust/rust';
import 'codemirror/mode/pascal/pascal';
import 'codemirror/mode/swift/swift';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
