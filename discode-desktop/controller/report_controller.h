#ifndef DISCODE_DESKTOP_REPORT_CONTROLLER_H
#define DISCODE_DESKTOP_REPORT_CONTROLLER_H

#include <QObject>
#include <QString>

#include <network_helpers.h>

class report_controller : public QObject {
    Q_OBJECT
public slots:
    // Get reporter id through session service
    void onReportReviewed(long long);

};


#endif //DISCODE_DESKTOP_REPORT_CONTROLLER_H
