var restauranteControllers = angular.module('restauranteControllers', []);

restauranteApp.controller('TableController', ['$scope', 'FreeTable', 'Date', 'Seatmap', '$http', '$resource', function($scope, FreeTable, Date, Seatmap, $http, $resource){
    Seatmap.registerClickHandler();

    $scope.hourChange = function(){
        $("#noTablesError").hide();
        Seatmap.deselectTables();
        var tableFrom = Date.getRestDate($scope.hour);
        var tableTo = Date.getRestDate(parseInt($scope.hour)+1);
        $scope.tables = FreeTable.query({from: tableFrom, to: tableTo});
        var loader = $('#loader');
        var room = $('#room');
        room.fadeOut();
        $('#selectHourInfo').hide();
        loader.show();
        var tablesSvg = document.getElementById("seats").contentDocument;

        angular.forEach($scope.tables, function(value, key)
            {
                var tableId = "table-"+value.tableID;
                if(value.seats == 0){
                    var table = $(tablesSvg.getElementById(tableId));
                    table.addClass('table-taken');
                    table.attr('available', false);
                }
            }
        );
        setTimeout(function(){loader.fadeOut(200); room.fadeIn(200)}, 1000);
    }

    $scope.reserve = function(){
        var IDsToReserve = Seatmap.getSelectedTablesIDs();
        if(IDsToReserve.length == 0) {
            $("#noTablesError").show();
            return;
        }
        $('#waitingModal').modal('show');
    }
}]);

restauranteControllers.controller('DashboardController', ['$scope', 'Book', 'Date', function($scope, Book, Date){
    var bookings = Book.getLatestBookings().query({from: Date.getRestDate("00")});
    $scope.dashboard = {
        bookings: bookings,
        totalBookings: 101
    };
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