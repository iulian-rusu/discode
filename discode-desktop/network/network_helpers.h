#ifndef DISCODE_DESKTOP_NETWORK_HELPERS_H
#define DISCODE_DESKTOP_NETWORK_HELPERS_H

#include "api_config.h"
#include "service_ips.h"
#include "service_routes.h"

#include <algorithm>
#include <unordered_map>
#include <ranges>
#include <string>
#include <sstream>

#include <QNetworkRequest>
#include <QString>
#include <QUrl>

namespace network_helpers {
    static QUrl get_url(api_config ac, service_ips ip, service_routes route, std::unordered_map<std::string, std::string> replacements = {}) {
        auto const ip_string{ac.get(ip)};
        auto const route_string{ac.get(route)};
        auto url_string{QString::fromStdString(ip_string + route_string)};

        std::ranges::for_each(replacements, [&url_string](auto const &pair) {
            url_string.replace(QString::fromStdString(pair.first), QString::fromStdString(pair.second));
        });

        return url_string;
    }

    static QNetworkRequest get_authentication_request(api_config ac) {
        static QNetworkRequest request;
        if (request.url().isEmpty()) {
            auto _ = get_url(ac, service_ips::Backend, service_routes::Authenticate).toString().toStdString();
            static QUrl const url{get_url(ac, service_ips::Backend, service_routes::Authenticate)};
            request.setUrl(url);
            request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
            request.setRawHeader(QByteArray{"Accept"}, "*/*");
            request.setRawHeader(QByteArray{"Cache-Control"}, "no-cache");
        }
        return request;
    }

    static QByteArray get_authentication_payload(std::string username, std::string password) {
        std::ostringstream buffer{};
        buffer << R"({"username": ")" << username << R"(","password": ")" << password << "\"}";
        return QByteArray::fromStdString(buffer.str());
    }
};


#endif //DISCODE_DESKTOP_NETWORK_HELPERS_H
