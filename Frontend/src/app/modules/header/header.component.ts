import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgClass, NgIf} from "@angular/common";

@Component({
    selector: 'app-header',
    standalone: true,
    imports: [
        RouterLink,
        NgIf,
        NgClass,
        RouterLinkActive
    ],
    templateUrl: './header.component.html',
    styleUrl: './header.component.scss'
})
export class HeaderComponent
{
    expandHeader = false;

    toggleHeader()
    {
        this.expandHeader = !this.expandHeader;
    }
}
