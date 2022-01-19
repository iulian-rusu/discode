#ifndef DISCODE_DESKTOP_AUTHENTICATION_SERVICE_H
#define DISCODE_DESKTOP_AUTHENTICATION_SERVICE_H

#include "api_config.h"

#include <functional>

using on_success_callback = std::function<void(std::string)>;
using on_failure_callback = std::function<void()>;

class authentication_service {
public:
    explicit authentication_service(api_config ac);
    void authenticate(std::string username, std::string password, on_success_callback on_success, on_failure_callback on_failure);
private:
    api_config ac;
};


#endif //DISCODE_DESKTOP_AUTHENTICATION_SERVICE_H
