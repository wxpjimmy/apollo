appService.service('UserService', ['$resource', '$q', function ($resource, $q) {
    var user_resource = $resource('', {}, {
        load_user: {
            method: 'GET',
            url: '/user'
        },
        find_users: {
            method: 'GET',
            url: '/users'
        },
        login_status: {
            method: 'GET',
            url: '/login/status'
        }
    });
    return {
        load_user: function () {
            var finished = false;
            var d = $q.defer();
            user_resource.load_user({},
                                    function (result) {
                                        finished = true;
                                        d.resolve(result);
                                    },
                                    function (result) {
                                        finished = true;
                                        d.reject(result);
                                    });
            return d.promise;
        },
        find_users: function (keyword) {
            var d = $q.defer();
            user_resource.find_users({
                                         keyword: keyword
                                     },
                                     function (result) {
                                         d.resolve(result);
                                     },
                                     function (result) {
                                         d.reject(result);
                                     });
            return d.promise;
        },

        login_status: function () {
            var finished = false;
            var d = $q.defer();
            user_resource.login_status({},
                                        function (result) {
                                            finished = true;
                                            d.resolve(result);
                                        },
                                        function (result) {
                                            finished = false;
                                            d.reject(result);
                                        }
            );
            return d.promise;
        }
    }
}]);
