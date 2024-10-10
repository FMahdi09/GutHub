import {Routes} from '@angular/router';
import {HomeComponent} from "./modules/home/home.component";
import {ExploreComponent} from "./modules/explore/explore.component";
import {FAQComponent} from "./modules/faq/faq.component";
import {LoginComponent} from "./modules/login/login.component";
import {RegisterComponent} from "./modules/register/register.component";

export const routes: Routes = [
    {
        path: "",
        component: HomeComponent
    },
    {
        path: "explore",
        component: ExploreComponent,
    },
    {
        path: "faq",
        component: FAQComponent
    },
    {
        path: "login",
        component: LoginComponent
    },
    {
        path: "register",
        component: RegisterComponent
    }
];
