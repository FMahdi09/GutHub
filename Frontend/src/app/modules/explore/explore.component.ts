import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
    selector: 'app-explore',
    standalone: true,
    imports: [
        RouterLink,
        RouterLinkActive
    ],
    templateUrl: './explore.component.html',
    styleUrl: './explore.component.scss'
})
export class ExploreComponent
{

}
