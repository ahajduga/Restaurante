var restauranteServices = angular.module('restauranteServices', ['ngResource']);

restauranteServices.factory('FreeTable', ['$resource',
    function($resource){
        return $resource('/getFreeTables?from=:from&to=:to');
    }]);

restauranteServices.factory('Date', function(){
    return {
        getDate: function(){
            return new Date().toJSON().slice(0,10);
        },
        getRestDate: function(hour){
            return this.getDate() + "-" + hour;
        }
    };
});

restauranteServices.factory('Seatmap', function(){
    return{
        getSeatmap: function(){
            return $(document.getElementById("seats").contentDocument);
        },
        getTables: function(){
            return $(document.getElementById("seats").contentDocument.getElementsByClassName("table"));
        },
        registerClickHandler: function(){
            var that = this;
            $(this.getTables()).click(
                function () {
                    var chosenTable = $(this);
                    that.selectTable(chosenTable);
                }
            );
        },
        deselectTables: function(){
            var tables = this.getTables();
            angular.forEach(tables, function(value, key)
                {
                    $(value).removeClass('table-selected');
                }
            );
        },
        getSelectedTablesIDs: function(){
            var IDs = [];
            var selectedTables = $(document.getElementById("seats").contentDocument.getElementsByClassName("table-selected"));
            angular.forEach(selectedTables, function(value, key){
                var splitID = $(value).attr("id").split("-").pop();;
                IDs.push(splitID);
            });
            return IDs;
        },
        selectTable: function(table){
            if (table.attr('available') == 'false') {
                toastr.options = {
                    "closeButton": true
                }
                toastr.error("Ten stolik jest już zajęty");
            }
            else {
                if (table.attr('selected') == 'false') {
                    table.attr('selected', true);
                    table.addClass('table-selected');
                    $("#noTablesError").hide();
                }
                else {
                    table.attr('selected', false);
                    table.removeClass('table-selected')
                }
            }
        }
    };
});

restauranteServices.factory('Book', ['$resource', function($resource){
    return {
        getLatestBookings: function(){
            return $resource('/getLatestReservations?from=:from');
        },
        getBookings: function(){
            return $resource('/book/:userID?from=:from&to=:to');
        }
    }
}]);

restauranteServices.factory('Staff', ['$resource', function($resource){
    return {
        getStaff: function(){
            return $resource('/users/:login');
        }
    }
}]);

restauranteServices.factory('Reservation', ['$resource', function($resource){
    return {
        getActiveReservations: function(){
            return $resource('/getActiveReservations');
        },
        deactivateReservation: function(){
            return $resource('/disableReservation?booking_ID=:id');
        }
    }
}]);