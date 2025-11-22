import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MetaComponent } from './pages/meta/meta.component';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MetaComponent, FormsModule, CardModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'login-page';
}
