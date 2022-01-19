#ifndef DISCODE_DESKTOP_REPORT_SERVICE_H
#define DISCODE_DESKTOP_REPORT_SERVICE_H

#include "api_config.h"
#include "report.h"
#include "session.h"

#include <functional>
#include <memory>
#include <optional>
#include <vector>

namespace report_space {
    using on_failure_callback = std::function<void()>;
    using on_get_callback = std::function<void(std::vector<report>)>;
    using on_success_callback = std::function<void()>;

    class report_service {
    public:
        explicit report_service(std::shared_ptr<api_config>);
        void review(long long, session, on_success_callback, on_failure_callback);
        void get(session, on_get_callback, on_failure_callback);
    private:
        std::shared_ptr<api_config> ac{};
    };
}


#endif //DISCODE_DESKTOP_REPORT_SERVICE_H
