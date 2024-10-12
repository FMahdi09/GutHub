import {Component, Input} from '@angular/core';

@Component({
    selector: 'app-list-item',
    standalone: true,
    imports: [],
    templateUrl: './list-item.component.html',
    styleUrl: './list-item.component.scss'
})
export class ListItemComponent
{
    @Input({required: true}) title!: string;

    @Input({required: true}) body!: string;
}
