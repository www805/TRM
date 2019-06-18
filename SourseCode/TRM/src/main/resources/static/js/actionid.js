

function getactionid_manage() {
    return {
        /*sweb*/
        main_getKeyword:"main_getKeyword",
        main_getRole:"main_getRole",
        main_logout:"main_logout",
        main_getUser:"main_getUser",
        main_tocaseIndex:"main_tocaseIndex",
        main_getPermissionsByMenu:"main_getPermissionsByMenu",

        home_getHome:"home_getHome",

        login_login:"login_login",
        login_main:"login_main",

        getRoleList_getRoleList:"getRoleList_getRoleList",
        getRoleList_getAddOrUpdateRole:"getRoleList_getAddOrUpdateRole",
        getRoleList_deleteRole:"getRoleList_deleteRole",
        getRoleList_changeboolRole:"getRoleList_changeboolRole",

        addOrUpdateRole_getRoleBySsid:"addOrUpdateRole_getRoleBySsid",//pp
        addOrUpdateRole_addRole:"addOrUpdateRole_addRole",//pp
        addOrUpdateRole_updateRole:"addOrUpdateRole_updateRole",


        getUserList_getUserList:"getUserList_getUserList",
        getUserList_getAddOrUpdateUser:"getUserList_getAddOrUpdateUser",
        getUserList_deleteUser:"getUserList_deleteUser",
        getUserList_getRoles:"getUserList_getRoles",//pp
        getUserList_getWorkunits:"getUserList_getWorkunits",//pp
        getUserList_changeboolUser:"getUserList_changeboolUser",//pp

        addOrUpdateUser_getUserBySsid:"addOrUpdateUser_getUserBySsid",//pp
        addOrUpdateUser_getWorkunits:"addOrUpdateUser_getWorkunits",//pp
        addOrUpdateUser_getRoles:"addOrUpdateUser_getRoles",//pp
        addOrUpdateUser_addUser:"addOrUpdateUser_addUser",//pp
        addOrUpdateUser_updateUser:"addOrUpdateUser_updateUser",



        getKeyword_getKeywordList:"getKeyword_getKeywordList",
        getKeyword_getAddOrUpdateKeyword:"getKeyword_getAddOrUpdateKeyword",
        getKeyword_deleteKeyword:"getKeyword_deleteKeyword",
        getKeyword_updateShieldbool:"getKeyword_updateShieldbool",

        addOrUpdateKeyword_getAddOrUpdateKeyword: "addOrUpdateKeyword_getAddOrUpdateKeyword",
        addOrUpdateKeyword_getKeyword: "addOrUpdateKeyword_getKeyword",

        serverconfig_uploadByImg: "serverconfig_uploadByImg",
        serverconfig_getServerConfig: "serverconfig_getServerConfig",
        serverconfig_uploadByClientImg: "serverconfig_uploadByClientImg",

        arraignment_count_getArraignment_countList: "arraignment_count_getArraignment_countList",
        arraignment_count_getArraignment_countPrint: "arraignment_count_getArraignment_countPrint ",

        arraignment_getArraignmentList:"arraignment_getArraignmentList",
        arraignment_getArraignmentByCaseSsid:"arraignment_getArraignmentByCaseSsid",
        arraignment_getArraignmentShow:"arraignment_getArraignmentShow",

        getArraignmentShow_getArraignmentBySsid:"getArraignmentShow_getArraignmentBySsid",

        permissionsShow_getRoles:"permissionsShow_getRoles",
        permissionsShow_getPermissions:"permissionsShow_getPermissions",
        permissionsShow_getPermissionsByRoleSsid:"permissionsShow_getPermissionsByRoleSsid",
        permissionsShow_updateRoleToPermissions:"permissionsShow_updateRoleToPermissions",

        downServer_getdownServers:"downServer_getdownServers",
        downServer_tostartDownServer:"downServer_tostartDownServer",

        startDownServer_startdownServer:"startDownServer_startdownServer",
        startDownServer_closeddownServer:"startDownServer_closeddownServer",
        startDownServer_getdataInfos:"startDownServer_getdataInfos",

        /*police*/
        login_userlogin:"login_userlogin",
        login_gotomain:"login_gotomain",
        login_gotologin:"login_gotologin",

        main_userloginout:"main_userloginout",
        main_toaddCaseToUser:"main_toaddCaseToUser",
        mian_home:"mian_home",
        main_torecordIndex:"main_torecordIndex",
        main_toTemplateIndex:"main_toTemplateIndex",
        main_totemplateTypeList:"main_totemplateTypeList",
        main_toproblemTypeList:"main_toproblemTypeList",
        main_torecordTypeList:"main_torecordTypeList",
        main_gotoupdateServerconfig:"main_gotoupdateServerconfig",

        addCaseToUser_addCaseToArraignment:"addCaseToUser_addCaseToArraignment",
        addCaseToUser_towaitRecord:"addCaseToUser_towaitRecord",

        updateServerconfig_getServerconfig:"updateServerconfig_getServerconfig",
        updateServerconfig_updateServerconfig:"updateServerconfig_updateServerconfig",

        recordTypeList_getRecordtypes:"recordTypeList_getRecordtypes",
        recordTypeList_getPidRecordtypes:"recordTypeList_getPidRecordtypes",
        recordTypeList_getRecordtypeById:"recordTypeList_getRecordtypeById",
        recordTypeList_updateRecordtype:"recordTypeList_updateRecordtype",
        recordTypeList_addRecordtype:"recordTypeList_addRecordtype",

        addCaseToUser_getRecordtypes:"addCaseToUser_getRecordtypes",
        addCaseToUser_getNationalitys:"addCaseToUser_getNationalitys",
        addCaseToUser_getNationals:"addCaseToUser_getNationals",
        addCaseToUser_getCards:"addCaseToUser_getCards",
        addCaseToUser_getUserByCard:"addCaseToUser_getUserByCard",
        addCaseToUser_getCaseById:"addCaseToUser_getCaseById",
        addCaseToUser_toaddCaseToUserDetail:"addCaseToUser_toaddCaseToUserDetail",
        addCaseToUser_addCase:"addCaseToUser_addCase",
        addCaseToUser_getUserinfoList:"addCaseToUser_getUserinfoList",
        addCaseToUser_getAdminList:"addCaseToUser_getAdminList",

        problemIndex_getProblems:"problemIndex_getProblems",
        problemIndex_getProblemTypes:"problemIndex_getProblemTypes",
        problemIndex_toaddOrupdateProblem:"problemIndex_toaddOrupdateProblem",
        addOrupdateProblem_getProblemById:"addOrupdateProblem_getProblemById",
        addOrupdateProblemType_getProblemTypeById:"addOrupdateProblemType_getProblemTypeById",
        addOrupdateProblem_updateProblem:"addOrupdateProblem_updateProblem",
        addOrupdateProblemType_addProblemType:"addOrupdateProblemType_addProblemType",
        addOrupdateProblemType_updateProblemType:"addOrupdateProblemType_updateProblemType",

        recordIndex_getRecords:"recordIndex_getRecords",
        recordIndex_getRecordtypes:"recordIndex_getRecordtypes",
        recordIndex_togetRecordById:"recordIndex_togetRecordById",
        recordIndex_towaitRecord:"recordIndex_towaitRecord",

        getRecordById_getRecordById:"getRecordById_getRecordById",
        getRecordById_tomoreRecord:"getRecordById_tomoreRecord",
        getRecordById_exportWord:"getRecordById_exportWord",
        getRecordById_exportPdf:"getRecordById_exportPdf",

        moreRecord_getRecordtypes:"moreRecord_getRecordtypes",//pp
        moreRecord_getRecords:"moreRecord_getRecords",

        waitRecord_tomoreTemplate:"waitRecord_tomoreTemplate",
        waitRecord_getTemplateById:"waitRecord_getTemplateById",
        waitRecord_addRecord:"waitRecord_addRecord",
        waitRecord_getRecordById:"waitRecord_getRecordById",
        waitRecord_startRercord:"waitRecord_startRercord",
        waitRecord_overRercord:"waitRecord_overRercord",
        waitRecord_getRercordAsrTxtBack:"waitRecord_getRercordAsrTxtBack",
        waitRecord_getTime:"waitRecord_getTime",
        waitRecord_exportWord:"waitRecord_exportWord",
        waitRecord_exportPdf:"waitRecord_exportPdf",
        waitRecord_updateArraignment:"waitRecord_updateArraignment",
        waitRecord_getNotifications:"waitRecord_getNotifications",
        waitRecord_downloadNotification:"waitRecord_downloadNotification",
        waitRecord_getEquipmentsState:"waitRecord_getEquipmentsState",
        waitRecord_togetPolygraph:"waitRecord_togetPolygraph",

        moreTemplate_getTemplateTypes:"moreTemplate_getTemplateTypes",
        moreTemplate_getTemplates:"moreTemplate_getTemplates",


        templateTypeList_getTemplateTypes: "templateTypeList_getTemplateTypes",
        templateTypeList_getTemplateTypeById: "templateTypeList_getTemplateTypeById",
        templateTypeList_addTemplateType: "templateTypeList_addTemplateType",
        templateTypeList_updateTemplateType: "templateTypeList_updateTemplateType",
        problemIndex_updateProblemType: "problemIndex_updateProblemType",
        problemIndex_addProblemType: "problemIndex_addProblemType",
        problemIndex_getTemplateTypeById: "problemIndex_getTemplateTypeById",
        addOrupdateTemplateType_addTemplateType: "addOrupdateTemplateType_addTemplateType",
        addOrupdateTemplateType_updateTemplateType: "addOrupdateTemplateType_updateTemplateType",
        addOrupdateTemplateType_getTemplateTypeById: "addOrupdateTemplateType_getTemplateTypeById",
        addOrupdateTemplateType_getTemplateTypes:"addOrupdateTemplateType_getTemplateTypes",
        addOrupdateTemplateType_getProblemTypes:"addOrupdateTemplateType_getProblemTypes",
        addOrupdateTemplateType_getProblemById:"addOrupdateTemplateType_getProblemById",

        templateIndex_getTemplates: "templateIndex_getTemplates",
        templateIndex_getTemplateTypes: "templateIndex_getTemplateTypes",
        templateIndex_exportWord: "templateIndex_exportWord",
        templateIndex_exportExcel: "templateIndex_exportExcel",
        templateIndex_uploadFile: "templateIndex_uploadFile",

        addOrupdateTemplate_getProblemTypes:"addOrupdateTemplate_getProblemTypes",
        addOrupdateTemplate_getTemplateTypes:"addOrupdateTemplate_getTemplateTypes",
        addOrupdateTemplate_getProblems: "addOrupdateTemplate_getProblems",
        addOrupdateTemplate_getTemplateById: "addOrupdateTemplate_getTemplateById",
        addOrupdateTemplate_addTemplate: "addOrupdateTemplate_addTemplate",


        notification_getNotifications: "notification_getNotifications",
        notification_uploadNotification: "notification_uploadNotification",
        notification_downloadNotification: "notification_downloadNotification",
        notification_deleteNotificationById: "notification_deleteNotificationById",

        addOrupdateProblem_addProblem: "addOrupdateProblem_addProblem",
        addOrupdateTemplate_updateProblem: "addOrupdateTemplate_updateProblem",
        addOrupdateTemplate_addProblem: "addOrupdateTemplate_addProblem",
        addOrupdateTemplate_getProblemById: "addOrupdateTemplate_getProblemById",
        addOrupdateTemplate_updateTemplate: "addOrupdateTemplate_updateTemplate",

        caseIndex_getCases:"caseIndex_getCases",
        caseIndex_toaddOrUpdateCase:"caseIndex_toaddOrUpdateCase",

        addOrUpdateCase_getCaseBySsid:"addOrUpdateCase_getCaseBySsid",
        addOrUpdateCase_updateCase:"addOrUpdateCase_updateCase",
        addOrUpdateCase_addCase:"addOrUpdateCase_addCase",
        addOrUpdateCase_getUserinfoList:"addOrUpdateCase_getUserinfoList",



    };
}