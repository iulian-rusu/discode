#ifndef DISCODE_DESKTOP_API_CONFIG_H
#define DISCODE_DESKTOP_API_CONFIG_H

#include "service_ips.h"
#include "service_routes.h"

#include <fstream>
#include <iostream>
#include <unordered_map>
#include <string>
#include <string_view>

struct api_config {
    api_config(std::string_view const &ips_path, std::string_view const &routes_path) {
        std::ifstream ips_ifstream{ips_path.data()};
        if (!ips_ifstream.is_open()) throw std::runtime_error("Cannot open ips config file.");
        std::ifstream routes_ifstream{routes_path.data()};
        if (!routes_ifstream.is_open()) throw std::runtime_error("Cannot open routes config file.");

        static std::unordered_map<std::string, service_ips> const string_to_ip{
                {"Backend", service_ips::Backend},
        };
        static std::unordered_map<std::string, service_routes> const string_to_route{
                {"Authenticate", service_routes::Authenticate},
                {"DeleteBan", service_routes::DeleteBan},
                {"GetBans", service_routes::GetBans},
                {"PostBan", service_routes::PostBan},
                {"GetReports", service_routes::GetReports},
                {"PatchReport", service_routes::PatchReport},
        };

        std::string key;
        std::string value;

        while (ips_ifstream >> key >> value) {
            try { ip_to_string.insert({string_to_ip.at(key), value}); }
            catch (std::out_of_range &) { std::cout << "Cannot parse ips config file line."; }
        }
        while (routes_ifstream >> key >> value) {
            try { route_to_string.insert({string_to_route.at(key), value}); }
            catch (std::out_of_range &) { std::cout << "Cannot parse routes config file line."; }
        }
    }

    std::string const &get(service_ips ip) {
        return ip_to_string.at(ip);
    }

    std::string const &get(service_routes route) {
        return route_to_string.at(route);
    }

    std::unordered_map<service_ips, std::string> ip_to_string;
    std::unordered_map<service_routes, std::string> route_to_string;
};

#endif //DISCODE_DESKTOP_API_CONFIG_H
