var restauranteApp = angular.module('restauranteApp', [
    'ngRoute',
    'restauranteControllers',
    'restauranteServices'
]);

restauranteApp.config(function($routeProvider) {
    $routeProvider

        // route for the home page
        .when('/', {
            templateUrl : 'views/dashboard/main.html',
            controller  : 'DashboardController'
        })

        // route for the about page
        .when('/staff', {
            templateUrl : 'views/dashboard/staff.html',
            controller  : 'StaffController'
        })

        .when('/reservations', {
            templateUrl : 'views/dashboard/reservations.html',
            controller  : 'ReservationController'
        })

        // route for the contact page
        .when('/contact', {
            templateUrl : 'pages/contact.html',
            controller  : 'DashboardController'
        });
});