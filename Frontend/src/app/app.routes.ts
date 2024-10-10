import {Routes} from '@angular/router';
import {HomeComponent} from "./modules/home/home.component";
import {ExploreComponent} from "./modules/explore/explore.component";
import {FAQComponent} from "./modules/faq/faq.component";
import {LoginComponent} from "./modules/login/login.component";
import {RegisterComponent} from "./modules/register/register.component";
import {UserListComponent} from "./modules/explore/user-list/user-list.component";
import {RepositoryListComponent} from "./modules/explore/repository-list/repository-list.component";
import {OrganizationListComponent} from "./modules/explore/organization-list/organization-list.component";

export const routes: Routes = [
    {
        path: "",
        component: HomeComponent
    },
    {
        path: "explore",
        component: ExploreComponent,
        children:
            [
                {
                    path: "",
                    redirectTo: "repositories",
                    pathMatch: "full"
                },
                {
                    path: "users",
                    component: UserListComponent
                },
                {
                    path: "repositories",
                    component: RepositoryListComponent
                },
                {
                    path: "organizations",
                    component: OrganizationListComponent
                }
            ]
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
