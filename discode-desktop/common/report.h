#ifndef DISCODE_DESKTOP_REPORT_H
#define DISCODE_DESKTOP_REPORT_H

#include <string>

struct report {
    report() = default;
    report(long long i_messageId, long long i_userId, std::string i_message, std::string i_reason, std::string i_issuer)
        : messageId(i_messageId), userId(i_userId), message(std::move(i_message)), reason(i_reason), issuer(std::move(i_issuer)) {}
    long long messageId{};
    long long userId{};
    std::string message{};
    std::string reason{};
    std::string issuer{};
};

#endif //DISCODE_DESKTOP_REPORT_H
