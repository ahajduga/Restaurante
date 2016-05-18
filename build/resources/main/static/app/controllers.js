var restauranteControllers = angular.module('restauranteControllers', []);

restauranteApp.controller('TableController', ['$scope', 'FreeTable', 'Date', 'Seatmap', 'Book', '$http', '$resource', '$window', function($scope, FreeTable, Date, Seatmap, Book, $http, $resource, $window){
    Seatmap.registerClickHandler();

    $scope.tablesObjects = [];

    $scope.hourChange = function(){
        $("#noTablesError").hide();
        Seatmap.deselectTables();
        var tableFrom = Date.getRestDate($scope.hour);
        var tableTo = Date.getRestDate(23);

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
            console.log(tables);
            angular.forEach(tables, function(value, key)
                {
                    console.log("DUUUUPA");
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

    $scope.reserve = function(){
        var IDsToReserve = Seatmap.getSelectedTablesIDs();

        if(IDsToReserve.length == 0) {
            $("#noTablesError").show();
            return;
        }
        $('#waitingModal').modal('show');

        angular.forEach(IDsToReserve, function(value, key){
            Book.bookTable().get({tableID: value, userID: 1, from: Date.getRestDate($scope.hour), to: Date.getRestDate(23)}, function(){


            });
        });

        $('#waitingModal').modal('hide');
        $('#confModal').modal('show');

        $scope.date = Date.getDate();

        var confTablesSvg = document.getElementById("confirmation-seats").contentDocument;

        angular.forEach(IDsToReserve, function(value, key){
            var tableId = "table-"+value;
            var table = $(confTablesSvg.getElementById(tableId));
            table.addClass('table-selected');
        });
    }

    $scope.endBookingProcess = function(){
        $window.location.href = "/bookend.html";
    }
}]);

restauranteControllers.controller('DashboardController', ['$scope', 'Book', 'Date', '$timeout', function($scope, Book, Date, $timeout){

    var bookings = Book.getLatestBookings().query({from: Date.getRestDate("00")});
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

    var recetBooksData = Book.getBookCount().query();
    var totalBookingsCount = Book.getTotalBookCount().query();

    $scope.dashboard = {
        bookings: bookings,
        totalBookings: totalBookingsCount,
        todaysBooks: recetBooksData.todayBookings,
        yesterdayBooks: recetBooksData.yesterdayBookings,
        percentage: recetBooksData.percentage
    };
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
    $scope.reservations = reservations;

    $scope.deactivate = function(id){
        Reservation.deactivateReservation().get({id: id}, function(){
            var reservationRow = $('#'+id);
            reservationRow.remove();
        });
    }
}]);