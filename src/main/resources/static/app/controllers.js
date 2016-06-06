var restauranteControllers = angular.module('restauranteControllers', []);

restauranteApp.controller('TableController', ['$scope', 'FreeTable', 'Date', 'Seatmap', 'Book', '$http', '$resource', '$window', function($scope, FreeTable, Date, Seatmap, Book, $http, $resource, $window){
    $('#reservationDate').datepicker({
        format: "yyyy-mm-dd",
        weekStart: 1,
        language: "pl",
        startDate: "2016-06-01"
    });

    Seatmap.registerClickHandler();

    $scope.tablesObjects = [];

    $scope.dateChange = function(){
        $('#hourSelect').show();
        if(typeof($scope.hour) != 'undefined'){
            Seatmap.deselectTables();
            var tableFrom = Date.getSpecificDate($scope.date, $scope.hour);
            var tableTo = Date.getSpecificDate($scope.date, 23);

            var tables = FreeTable.query({from: tableFrom, to: tableTo}, function(){
                tables = $.map(tables, function(value, key){
                    return [value];
                });
                tables = tables.slice(0, 9);
                $scope.tables = tables;

                var loader = $('#loader');
                var room = $('#room');
                room.fadeOut();
                $('#selectHourInfo').hide();
                loader.show();
                var tablesSvg = document.getElementById("seats").contentDocument;
                Seatmap.makeAllTablesTaken();
                angular.forEach(tables, function(value, key)
                    {
                        var tableId = "table-"+value.tableID;
                        /*if(value.seats == 0){
                         var table = $(tablesSvg.getElementById(tableId));
                         table.addClass('table-taken');
                         table.attr('available', false);
                         }*/
                        var table = $(tablesSvg.getElementById(tableId));
                        console.log(table);
                        table.removeClass('table-taken');
                        table.attr('available', true);
                    }
                );
                setTimeout(function(){loader.fadeOut(200); room.fadeIn(200)}, 1000);
            });
        }

    };

    $scope.hourChange = function(){
        $("#noTablesError").hide();
        Seatmap.deselectTables();
        var tableFrom = Date.getSpecificDate($scope.date, $scope.hour);
        var tableTo = Date.getSpecificDate($scope.date, 23);

        var tables = FreeTable.query({from: tableFrom, to: tableTo}, function(){
            tables = $.map(tables, function(value, key){
                return [value];
            });
            tables = tables.slice(0, 9);
            $scope.tables = tables;

            var loader = $('#loader');
            var room = $('#room');
            room.fadeOut();
            $('#selectHourInfo').hide();
            loader.show();
            var tablesSvg = document.getElementById("seats").contentDocument;
            Seatmap.makeAllTablesTaken();
            angular.forEach(tables, function(value, key)
                {
                    var tableId = "table-"+value.tableID;
                    /*if(value.seats == 0){
                     var table = $(tablesSvg.getElementById(tableId));
                     table.addClass('table-taken');
                     table.attr('available', false);
                     }*/
                    var table = $(tablesSvg.getElementById(tableId));
                    console.log(table);
                    table.removeClass('table-taken');
                    table.attr('available', true);
                }
            );
            setTimeout(function(){loader.fadeOut(200); room.fadeIn(200)}, 1000);
        });
    };

    $scope.reserve = function(){
        var IDsToReserve = Seatmap.getSelectedTablesIDs();

        $('#waitingModal').modal('hide');
        $('#confModal').modal('show');

        //$scope.date = $scope.date;

        var confTablesSvg = document.getElementById("confirmation-seats").contentDocument;

        angular.forEach(IDsToReserve, function(value, key){
            var tableId = "table-"+value;
            var table = $(confTablesSvg.getElementById(tableId));
            table.addClass('table-selected');
        });
    };

    $scope.endBookingProcess = function(){
        $window.location.href = "/bookend.html";
        var IDsToReserve = Seatmap.getSelectedTablesIDs();
        angular.forEach(IDsToReserve, function(value, key){
            Book.bookTable().get({tableID: value, userID: 1, from: Date.getSpecificDate($scope.date, $scope.hour), to: Date.getSpecificDate($scope.date, 23), mail: $scope.mail}, function(){

            });
        });
    };
}]);

restauranteControllers.controller('DashboardController', ['$scope', 'Book', 'Date', '$timeout', function($scope, Book, Date, $timeout){

    var bookings = Book.getLatestBookings().query({from: Date.getSpecificDate("2016-06-01", "00")});
    var labels;
    var data;
    var chartData = Book.getChartData().query(function(){
        labels = chartData[0];
        data = chartData[1];
        labels = $.map(labels, function(value, index){
            return [value];
        });
        labels = labels.slice(0, 6);

        data = $.map(data, function(value, index){
            return [value];
        });
        data = data.slice(0, 6);
    });

    //var recetBooksData = Book.getBookCount().get(function(){
    //    console.log(recentBooksData);
    //});

    $scope.dashboard = {
        bookings: bookings,
        totalBookings: 0,
        todaysBooks: 0,
        yesterdayBooks: 0,
        percentage: 0
    };

    $.get("count", function(data){
        $scope.dashboard.totalBookings = data;
    });

    $.get("recent", function(recentData){
        console.log(recentData['todaysBooks']);
        $scope.dashboard.todaysBooks = recentData.todayBookings;
        $scope.dashboard.yesterdayBooks = recentData.yesterdayBookings;
        $scope.dashboard.percentage = recentData.percentage;
    });

    //console.log($.makeArray(chartData));
    var ctx;
    setTimeout(function(){
        ctx = $("#lineChart");
        var myNewChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: "Założone rezerwacje",
                        data: data
                    }
                ],
            },
            options: {
                legend:{
                    display: false
                }
            }
        });
    }, 3000);

}]);

restauranteControllers.controller('StaffController', ['$scope', 'Staff', function($scope, Staff){
    var users = Staff.getStaff().query();
    $scope.staff = users;

    $scope.delete = function(id, login){
        Staff.getStaff().delete({login: login}, function(){
            var userPane = $('#user-'+id);
            $(userPane).addClass('animated fadeOut');
            $(userPane).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){$(userPane).remove();});
        });
    }
}]);

restauranteControllers.controller('ReservationController', ['$scope', 'Reservation', function($scope, Reservation){
    var reservations = Reservation.getActiveReservations().query();
    console.log(reservations);
    //reservations = reservations[Object.keys(reservations)[0]];
    //console.log(reservations);
    $scope.reservations = reservations;

    $scope.deactivate = function(id){
        Reservation.deactivateReservation().get({id: id}, function(){
            var reservationRow = $('#'+id);
            reservationRow.remove();
        });
    }
}]);