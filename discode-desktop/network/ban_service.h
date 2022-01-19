#ifndef DISCODE_DESKTOP_BAN_SERVICE_H
#define DISCODE_DESKTOP_BAN_SERVICE_H

#include "api_config.h"
#include "ban.h"
#include "session.h"

#include <functional>
#include <memory>
#include <optional>
#include <vector>

namespace ban_space {
    using on_failure_callback = std::function<void()>;
    using on_get_callback = std::function<void(std::vector<ban>)>;
    using on_success_callback = std::function<void()>;

    class ban_service {
    public:
        explicit ban_service(std::shared_ptr<api_config>);
        void ban_user(long long, long long, std::string, session, on_success_callback, on_failure_callback);
        void get(session, on_get_callback, on_failure_callback);
        void unban(long long, session, on_success_callback, on_failure_callback);
    private:
        std::shared_ptr<api_config> ac{};
    };
}

#endif //DISCODE_DESKTOP_BAN_SERVICE_H
