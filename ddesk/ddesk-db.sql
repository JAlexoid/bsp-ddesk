--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: -
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- Name: certificates_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE certificates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: certificates_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('certificates_id_seq', 1, false);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: certificates; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE certificates (
    id integer DEFAULT nextval('certificates_id_seq'::regclass) NOT NULL,
    md5_key character(64) NOT NULL,
    person integer,
    name character varying(500) NOT NULL,
    cert text NOT NULL,
    valid boolean NOT NULL
);


--
-- Name: change_views_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE change_views_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: change_views_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('change_views_id_seq', 1, false);


--
-- Name: change_views; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE change_views (
    id integer DEFAULT nextval('change_views_id_seq'::regclass) NOT NULL,
    person integer,
    ticket_history integer,
    on_date timestamp without time zone NOT NULL
);


--
-- Name: companies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE companies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: companies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('companies_id_seq', 1, false);


--
-- Name: companies; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE companies (
    id integer DEFAULT nextval('companies_id_seq'::regclass) NOT NULL,
    code character varying(20) NOT NULL,
    vatcode character varying(20),
    name character varying(300) NOT NULL,
    adress character varying(1000) NOT NULL,
    billingadress character varying(1000) NOT NULL,
    distance integer,
    notes text
);


--
-- Name: companycontracts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE companycontracts_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: companycontracts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('companycontracts_id_seq', 1, true);


--
-- Name: companycontracts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE companycontracts (
    id integer DEFAULT nextval('companycontracts_id_seq'::regclass) NOT NULL,
    company integer,
    contractno character varying(20),
    contractdate date,
    expirydate date,
    servicecode character varying(20),
    serviceparams text,
    billsum integer,
    billingperiod integer,
    freehours integer,
    hourlyrate numeric(10,4) DEFAULT 0.0000,
    lastinvoicedate date,
    notes text,
    responcetime numeric(6,2)
);


--
-- Name: companypersons_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE companypersons_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: companypersons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('companypersons_id_seq', 2, true);


--
-- Name: companypersons; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE companypersons (
    id integer DEFAULT nextval('companypersons_id_seq'::regclass) NOT NULL,
    company integer,
    name character varying(250) NOT NULL,
    "position" character varying(200),
    phoneno character varying(50),
    email character varying(100),
    logincode character varying(50),
    password character varying(32),
    loginlevel integer,
    notes text,
    language character(6) DEFAULT 'lt_LT'::bpchar NOT NULL,
    lastlogin timestamp without time zone NOT NULL,
    search_filter text
);


--
-- Name: eventtypes_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE eventtypes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: eventtypes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('eventtypes_id_seq', 1, false);


--
-- Name: eventtypes; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE eventtypes (
    id integer DEFAULT nextval('eventtypes_id_seq'::regclass) NOT NULL,
    name character varying(100) NOT NULL
);


--
-- Name: logs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: logs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('logs_id_seq', 1, false);


--
-- Name: logs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE logs (
    id integer DEFAULT nextval('logs_id_seq'::regclass) NOT NULL,
    ticket integer,
    person integer,
    log text
);


--
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('permissions_id_seq', 1, false);


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE permissions (
    id integer DEFAULT nextval('permissions_id_seq'::regclass) NOT NULL,
    code character varying(200) NOT NULL,
    person integer NOT NULL
);


--
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE projects_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: projects_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('projects_id_seq', 1, true);


--
-- Name: projects; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE projects (
    id integer DEFAULT nextval('projects_id_seq'::regclass) NOT NULL,
    name character varying(200) NOT NULL,
    code character varying(200),
    company integer,
    person integer,
    description text,
    version numeric(10,7),
    superior integer,
    manager integer
);


--
-- Name: person_info; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW person_info AS
    SELECT p.id, p.name, p.email, pj.id AS project_id, pj.name AS project, pj.code, c.name AS company, c.id AS company_id FROM ((companypersons p JOIN companies c ON ((c.id = p.company))) LEFT JOIN projects pj USING (company));


--
-- Name: priorities_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE priorities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: priorities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('priorities_id_seq', 1, false);


--
-- Name: priorities; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE priorities (
    id integer DEFAULT nextval('priorities_id_seq'::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    timing numeric(6,2) NOT NULL
);


--
-- Name: project_info; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW project_info AS
    SELECT p.id, p.name, p.code, p.company, p.person, p.manager, p.description, p.version, c.name AS company_name, cp.name AS person_name, cp2.name AS manager_name, cp2.email AS manager_email FROM (((projects p LEFT JOIN companypersons cp ON ((p.person = cp.id))) LEFT JOIN companypersons cp2 ON ((p.manager = cp2.id))) LEFT JOIN companies c ON ((c.id = p.company)));


--
-- Name: project_modules_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE project_modules_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: project_modules_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('project_modules_id_seq', 1, true);


--
-- Name: project_modules; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE project_modules (
    id integer DEFAULT nextval('project_modules_id_seq'::regclass) NOT NULL,
    module character varying(200) NOT NULL,
    description text,
    project integer NOT NULL
);


--
-- Name: severities; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE severities (
    id integer NOT NULL,
    name character varying(100) NOT NULL
);


--
-- Name: statuses; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE statuses (
    id integer NOT NULL,
    name character varying(100) NOT NULL
);


--
-- Name: tickets_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tickets_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('tickets_id_seq', 7, true);


--
-- Name: tickets; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE tickets (
    id integer DEFAULT nextval('tickets_id_seq'::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    company integer,
    person integer,
    reportdate timestamp with time zone NOT NULL,
    reportby integer,
    description text,
    status integer NOT NULL,
    acceptednotes text,
    assigneddate timestamp with time zone,
    assignedto integer,
    assignedby integer,
    worktime numeric(10,4),
    additionaltime numeric(10,4),
    planeddate timestamp with time zone,
    actualdate timestamp with time zone,
    servicecode character varying(20),
    priority integer,
    type integer NOT NULL,
    resolution text,
    dateclosed timestamp with time zone,
    closedby integer,
    editdate timestamp with time zone,
    editby integer,
    uniqueid character varying(100) NOT NULL,
    chargeable boolean DEFAULT true NOT NULL,
    reporter_mail character varying(200),
    reporter_xmpp character varying(200),
    version numeric(10,7),
    project integer,
    module integer,
    severity integer,
    in_day integer,
    administrative boolean NOT NULL
);


--
-- Name: ticket_info; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW ticket_info AS
    SELECT sv.name AS severity_name, e.name AS eventname, p.name AS priorityname, cp.name AS personname, cp.email AS personemail, cp."position" AS personposition, cp.phoneno, pj.name AS project_name, pj.code AS project_code, pj.company AS projects_company, pj.person AS projects_person, pj.manager AS projects_manager, md.module AS module_name, s.name AS statusname, c.name AS companyname, cp2.name AS asignee, t.id, t.name, t.company, t.person, t.reportdate, t.reportby, t.description, t.status, t.acceptednotes, t.assigneddate, t.assignedto, t.assignedby, t.worktime, t.additionaltime, t.planeddate, t.actualdate, t.servicecode, t.priority, t.type, t.resolution, t.dateclosed, t.closedby, t.editdate, t.editby, t.uniqueid, t.chargeable, t.reporter_mail, t.reporter_xmpp, t.version, t.project, t.module, t.severity, t.administrative FROM (((((((((tickets t JOIN statuses s ON ((s.id = t.status))) LEFT JOIN priorities p ON ((t.priority = p.id))) JOIN eventtypes e ON ((e.id = t.type))) LEFT JOIN companies c ON ((c.id = t.company))) LEFT JOIN severities sv ON ((sv.id = t.severity))) LEFT JOIN projects pj ON ((pj.id = t.project))) LEFT JOIN project_modules md ON ((md.id = t.module))) LEFT JOIN companypersons cp ON ((cp.id = t.person))) LEFT JOIN companypersons cp2 ON ((t.assignedto = cp2.id)));


--
-- Name: tickethistory_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tickethistory_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: tickethistory_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('tickethistory_id_seq', 17, true);


--
-- Name: tickethistory; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE tickethistory (
    id integer DEFAULT nextval('tickethistory_id_seq'::regclass) NOT NULL,
    ticket integer NOT NULL,
    changenotes text,
    name character varying(100) NOT NULL,
    company integer,
    person integer,
    reportdate timestamp with time zone NOT NULL,
    reportby integer,
    description text,
    status integer NOT NULL,
    acceptednotes text,
    assigneddate timestamp with time zone,
    assignedto integer,
    assignedby integer,
    worktime numeric(10,4),
    additionaltime numeric(10,4),
    planeddate timestamp with time zone,
    actualdate timestamp with time zone,
    servicecode character varying(20),
    priority integer,
    type integer NOT NULL,
    resolution text,
    dateclosed timestamp with time zone,
    closedby integer,
    editdate timestamp with time zone,
    editby integer,
    notes text,
    notespublic text,
    version numeric(10,7),
    project integer,
    module integer,
    severity integer
);


--
-- Name: timekeeping_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE timekeeping_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: timekeeping_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('timekeeping_id_seq', 1, false);


--
-- Name: timekeeping; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE timekeeping (
    id integer DEFAULT nextval('timekeeping_id_seq'::regclass) NOT NULL,
    person integer,
    start timestamp with time zone,
    finish timestamp with time zone,
    name character varying(100),
    description text
);


--
-- Name: xmpp_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE xmpp_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: xmpp_logs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('xmpp_logs_id_seq', 1, false);


--
-- Name: xmpp_logs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE xmpp_logs (
    id bigint DEFAULT nextval('xmpp_logs_id_seq'::regclass) NOT NULL,
    ticket integer,
    project integer,
    name character varying(200) NOT NULL,
    log_time timestamp without time zone NOT NULL,
    messages text
);


--
-- Name: client_usage(timestamp without time zone, timestamp without time zone, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION client_usage(term_start timestamp without time zone, term_end timestamp without time zone, client integer) RETURNS SETOF record
    AS $_$SELECT

   c.id as id,

   c.name as name,

   c.code as code,

   c.vatcode as vatcode,

   cc.contractno as contractno,

   cc.contractdate as contractdate,

   cc.expirydate as expirydate,

   cc.servicecode as servicecode,

   cc.billsum as billsum,

   cc.billingperiod as billingperiod,

   cc.freehours as freehours,

   cc.hourlyrate as hourlyrate,

   cc.lastinvoicedate as lastinvoicedate,

   cc.responcetime as responcetime,

   count(t.uniqueid) as record_number,

   COALESCE(SUM(t.worktime), 0.0) + COALESCE(SUM(t.additionaltime), 0.0) as totaltime

FROM 

   companies c,

   companycontracts cc,

   tickets t

WHERE 

   t.company = c.id

   AND

   cc.company = c.id

   AND

   t.chargeable

   AND

   (t.dateclosed >= $1 OR $1 IS NULL)

   AND

   (t.dateclosed <= $2 OR $2 IS NULL)

   AND

   (c.id = $3 OR $3 IS NULL)

GROUP BY

   c.id,

   c.name,

   c.code,

   c.vatcode,

   cc.contractno,

   cc.contractdate,

   cc.expirydate,

   cc.servicecode,

   cc.billsum,

   cc.billingperiod,

   cc.freehours,

   cc.hourlyrate,

   cc.lastinvoicedate,

   cc.responcetime

ORDER BY

   c.id

   $_$
    LANGUAGE sql;


--
-- Name: unique_id_generator(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION unique_id_generator() RETURNS trigger
    AS $$	DECLARE

		numberInToday integer;

                projectCode  text;

   BEGIN

        IF NEW.uniqueId <> '' THEN

            RETURN NEW;

        END IF;

IF NEW.project IS NOT NULL AND NEW.project <> 0 THEN

     SELECT INTO projectCode code FROM projects WHERE id = NEW.project;

     SELECT INTO numberInToday COALESCE(MAX(in_day),0)+1 FROM tickets WHERE project = NEW.project;

     NEW.in_day := numberInToday;

     NEW.uniqueid := upper(projectCode)||'-'||NEW.in_day;

ELSE

         		  SELECT INTO numberInToday COALESCE(MAX(in_day),0)+1 FROM tickets WHERE reportdate::date = NEW.reportdate::date;

		  NEW.in_day := numberInToday;

		  NEW.uniqueId := 'U'||to_char(NEW.reportdate,'YYYYMMDD') || numberInToday;

END IF;

        RETURN NEW;

    END;$$
    LANGUAGE plpgsql;


--
-- Name: unique_id_refresher(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION unique_id_refresher() RETURNS trigger
    AS $$

DECLARE

numberInToday integer;

                projectCode  text;

   BEGIN

        IF (NEW.project = OLD.project OR (NEW.project IS NULL AND OLD.project IS NULL)) AND OLD.uniqueid <> '' THEN

            RETURN NEW;

        END IF;

IF NEW.project IS NOT NULL AND NEW.project <> 0 THEN

     SELECT INTO projectCode code FROM projects WHERE id = NEW.project;

     SELECT INTO numberInToday COALESCE(MAX(in_day),0)+1 FROM tickets WHERE project = NEW.project;

     NEW.in_day := numberInToday;

     NEW.uniqueid := upper(projectCode)||'-'||NEW.in_day;

ELSE

           SELECT INTO numberInToday COALESCE(MAX(in_day),0)+1 FROM tickets WHERE reportdate::date = NEW.reportdate::date;

  NEW.in_day := numberInToday;

  NEW.uniqueId := 'U'||to_char(NEW.reportdate,'YYYYMMDD') || numberInToday;

END IF;

        RETURN NEW;

    END;

$$
    LANGUAGE plpgsql;


--
-- Data for Name: certificates; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: change_views; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: companies; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO companies VALUES (0, 'WE', NULL, 'WE', 'N/A', 'N/A', NULL, NULL);


--
-- Data for Name: companycontracts; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: companypersons; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO companypersons VALUES (0, 0, 'N/A', 'N/A', NULL, '', NULL, NULL, NULL, '', 'lt_LT ', '2009-08-25 22:20:39.287', 'rO0ABXNyACZjb20uYmFsdGljc29sdXRpb25zLmhkZXNrLlRpY2tldEZpbHRlcgAAAC608ht6AgAaSQAIYWNjZXB0ZWRJAApjaGFyZ2VhYmxlSQAGY2xvc2VkTAAIYXNzaWduZWR0ABNMamF2YS9sYW5nL0ludGVnZXI7TAAHY29tcGFueXEAfgABTAAHZm9yRGF0ZXQAEExqYXZhL3V0aWwvRGF0ZTtMAAZtb2R1bGVxAH4AAUwABnBlcnNvbnEAfgABTAAIcHJpb3JpdHlxAH4AAUwAB3Byb2plY3RxAH4AAUwAC3NvcnRBc2lnbmVldAATTGphdmEvbGFuZy9Cb29sZWFuO0wAC3NvcnRDb21wYW55cQB+AANMAAxzb3J0RWRpdERhdGVxAH4AA0wACHNvcnROYW1lcQB+AANMAApzb3J0UGxhbmVkcQB+AANMAAxzb3J0UHJpb3JpdHlxAH4AA0wADnNvcnRSZXBvcnREYXRlcQB+AANMAApzb3J0U3RhdHVzcQB+AANMAAhzb3J0VHlwZXEAfgADTAAMc29ydFVuaXF1ZUlkcQB+AANMAAtzb3J0VmVyc2lvbnEAfgADTAAGc3RhdHVzcQB+AAFMAAp0aWNrZXROYW1ldAASTGphdmEvbGFuZy9TdHJpbmc7TAAEdHlwZXEAfgABTAAIdW5pcXVlSWRxAH4ABEwAB3ZlcnNpb250ABJMamF2YS9sYW5nL0RvdWJsZTt4cAAAAAAAAAAAAAAAAHBwcHBwcHBwcHBwcHBwcHBwcHB0AABwcQB+AAdw');
INSERT INTO companypersons VALUES (2, 0, 'Admin', 'Admin', '', 'alex@activelogic.eu', 'admin', '21232f297a57a5a743894a0e4a801fc3', 0, '', 'en_US ', '2009-08-29 22:31:25.45', 'rO0ABXNyACFsdC5ic3ByZW5kaW1haS5kZGVzay5UaWNrZXRGaWx0ZXIAAAAutPIbegIAGkkACGFjY2VwdGVkSQAKY2hhcmdlYWJsZUkABmNsb3NlZEwACGFzc2lnbmVkdAATTGphdmEvbGFuZy9JbnRlZ2VyO0wAB2NvbXBhbnlxAH4AAUwAB2ZvckRhdGV0ABBMamF2YS91dGlsL0RhdGU7TAAGbW9kdWxlcQB+AAFMAAZwZXJzb25xAH4AAUwACHByaW9yaXR5cQB+AAFMAAdwcm9qZWN0cQB+AAFMAAtzb3J0QXNpZ25lZXQAE0xqYXZhL2xhbmcvQm9vbGVhbjtMAAtzb3J0Q29tcGFueXEAfgADTAAMc29ydEVkaXREYXRlcQB+AANMAAhzb3J0TmFtZXEAfgADTAAKc29ydFBsYW5lZHEAfgADTAAMc29ydFByaW9yaXR5cQB+AANMAA5zb3J0UmVwb3J0RGF0ZXEAfgADTAAKc29ydFN0YXR1c3EAfgADTAAIc29ydFR5cGVxAH4AA0wADHNvcnRVbmlxdWVJZHEAfgADTAALc29ydFZlcnNpb25xAH4AA0wABnN0YXR1c3EAfgABTAAKdGlja2V0TmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO0wABHR5cGVxAH4AAUwACHVuaXF1ZUlkcQB+AARMAAd2ZXJzaW9udAASTGphdmEvbGFuZy9Eb3VibGU7eHAAAAAAAAAAAAAAAABwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==');


--
-- Data for Name: eventtypes; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO eventtypes VALUES (1, 'BUG');
INSERT INTO eventtypes VALUES (2, 'Feature');
INSERT INTO eventtypes VALUES (3, 'Other');
INSERT INTO eventtypes VALUES (0, 'N/A');


--
-- Data for Name: logs; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: priorities; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO priorities VALUES (2, 'Tomorrow', 24.00);
INSERT INTO priorities VALUES (7, 'Low', 1000.00);
INSERT INTO priorities VALUES (10, 'Lowest', 5000.00);
INSERT INTO priorities VALUES (1, 'Overnight', 0.00);
INSERT INTO priorities VALUES (4, 'High', 168.00);
INSERT INTO priorities VALUES (5, 'Normal', 720.00);
INSERT INTO priorities VALUES (3, 'This week', 96.00);


--
-- Data for Name: project_modules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO project_modules VALUES (1, 'CORE', NULL, 1);
INSERT INTO project_modules VALUES (0, 'N/A', 'N/A', 0);


--
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO projects VALUES (1, 'KnowDesk', 'KDSK', 0, NULL, NULL, 1.0000000, NULL, 2);
INSERT INTO projects VALUES (0, 'N/A', NULL, 0, 0, NULL, NULL, NULL, 0);


--
-- Data for Name: severities; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO severities VALUES (0, 'Change me');
INSERT INTO severities VALUES (1, 'Critical');
INSERT INTO severities VALUES (2, 'Severe');
INSERT INTO severities VALUES (3, 'Minor');
INSERT INTO severities VALUES (4, 'Cosmetic');


--
-- Data for Name: statuses; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO statuses VALUES (3, 'Confirmed');
INSERT INTO statuses VALUES (4, 'Started');
INSERT INTO statuses VALUES (5, 'In progress');
INSERT INTO statuses VALUES (6, 'Finished');
INSERT INTO statuses VALUES (1, 'Reported');
INSERT INTO statuses VALUES (2, 'Accepted');
INSERT INTO statuses VALUES (1000, 'Closed');
INSERT INTO statuses VALUES (0, 'N/A');


--
-- Data for Name: tickethistory; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: timekeeping; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: xmpp_logs; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Name: certificates_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY certificates
    ADD CONSTRAINT certificates_pkey PRIMARY KEY (id);


--
-- Name: change_views_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY change_views
    ADD CONSTRAINT change_views_pkey PRIMARY KEY (id);


--
-- Name: companies_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY companies
    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);


--
-- Name: companycontracts_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY companycontracts
    ADD CONSTRAINT companycontracts_pkey PRIMARY KEY (id);


--
-- Name: companypersons_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY companypersons
    ADD CONSTRAINT companypersons_pkey PRIMARY KEY (id);


--
-- Name: eventtypes_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY eventtypes
    ADD CONSTRAINT eventtypes_pkey PRIMARY KEY (id);


--
-- Name: logs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY logs
    ADD CONSTRAINT logs_pkey PRIMARY KEY (id);


--
-- Name: permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: priorities_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY priorities
    ADD CONSTRAINT priorities_pkey PRIMARY KEY (id);


--
-- Name: project_modules_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY project_modules
    ADD CONSTRAINT project_modules_pkey PRIMARY KEY (id);


--
-- Name: projects_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- Name: sever_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY severities
    ADD CONSTRAINT sever_pkey PRIMARY KEY (id);


--
-- Name: statuses_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY statuses
    ADD CONSTRAINT statuses_pkey PRIMARY KEY (id);


--
-- Name: tickethistory_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tickethistory
    ADD CONSTRAINT tickethistory_pkey PRIMARY KEY (id);


--
-- Name: tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- Name: timekeeping_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY timekeeping
    ADD CONSTRAINT timekeeping_pkey PRIMARY KEY (id);


--
-- Name: xmpp_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY xmpp_logs
    ADD CONSTRAINT xmpp_logs_pkey PRIMARY KEY (id);


--
-- Name: permissions_code_person; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX permissions_code_person ON permissions USING btree (code, person);


--
-- Name: tidchar; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX tidchar ON tickets USING btree (uniqueid);


--
-- Name: xif10tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif10tickets ON tickets USING btree (closedby);


--
-- Name: xif1companycontracts; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif1companycontracts ON companycontracts USING btree (company);


--
-- Name: xif1companypersons; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif1companypersons ON companypersons USING btree (company);


--
-- Name: xif1tickethistory; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif1tickethistory ON tickethistory USING btree (ticket);


--
-- Name: xif1tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif1tickets ON tickets USING btree (editby);


--
-- Name: xif2tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif2tickets ON tickets USING btree (company);


--
-- Name: xif3tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif3tickets ON tickets USING btree (person);


--
-- Name: xif4tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif4tickets ON tickets USING btree (priority);


--
-- Name: xif5tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif5tickets ON tickets USING btree (assignedto);


--
-- Name: xif6tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif6tickets ON tickets USING btree (status);


--
-- Name: xif7tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif7tickets ON tickets USING btree (type);


--
-- Name: xif8tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif8tickets ON tickets USING btree (assignedby);


--
-- Name: xif9tickets; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX xif9tickets ON tickets USING btree (reportby);


--
-- Name: refresher; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER refresher
    BEFORE UPDATE ON tickets
    FOR EACH ROW
    EXECUTE PROCEDURE unique_id_refresher();


--
-- Name: uniqueidgenerator; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER uniqueidgenerator
    BEFORE INSERT ON tickets
    FOR EACH ROW
    EXECUTE PROCEDURE unique_id_generator();


--
-- Name: certificates_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY certificates
    ADD CONSTRAINT certificates_person_fkey FOREIGN KEY (person) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: change_views_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY change_views
    ADD CONSTRAINT change_views_person_fkey FOREIGN KEY (person) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: change_views_ticket_history_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY change_views
    ADD CONSTRAINT change_views_ticket_history_fkey FOREIGN KEY (ticket_history) REFERENCES tickethistory(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: companycontracts_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY companycontracts
    ADD CONSTRAINT companycontracts_company_fkey FOREIGN KEY (company) REFERENCES companies(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: companypersons_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY companypersons
    ADD CONSTRAINT companypersons_company_fkey FOREIGN KEY (company) REFERENCES companies(id);


--
-- Name: fkey_company_project; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT fkey_company_project FOREIGN KEY (company) REFERENCES companies(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: fkey_person_project; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT fkey_person_project FOREIGN KEY (person) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: fkey_tickethistory_module; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickethistory
    ADD CONSTRAINT fkey_tickethistory_module FOREIGN KEY (module) REFERENCES project_modules(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: fkey_tickethistory_project; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickethistory
    ADD CONSTRAINT fkey_tickethistory_project FOREIGN KEY (project) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fkey_tickets_module; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT fkey_tickets_module FOREIGN KEY (module) REFERENCES project_modules(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: fkey_tickets_project; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT fkey_tickets_project FOREIGN KEY (project) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: permissions_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY permissions
    ADD CONSTRAINT permissions_person_fkey FOREIGN KEY (person) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: project_module_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY project_modules
    ADD CONSTRAINT project_module_project_fkey FOREIGN KEY (project) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: projects_company_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_company_fkey FOREIGN KEY (company) REFERENCES companies(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: projects_manager_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_manager_fkey FOREIGN KEY (manager) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: projects_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_person_fkey FOREIGN KEY (person) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: projects_superior_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_superior_fkey FOREIGN KEY (superior) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: sever; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT sever FOREIGN KEY (severity) REFERENCES severities(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickethistory_ticket_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickethistory
    ADD CONSTRAINT tickethistory_ticket_fkey FOREIGN KEY (ticket) REFERENCES tickets(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: tickets_assignedby_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_assignedby_fkey FOREIGN KEY (assignedby) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_assignedto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_assignedto_fkey FOREIGN KEY (assignedto) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_closedby_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_closedby_fkey FOREIGN KEY (closedby) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_editby_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_editby_fkey FOREIGN KEY (editby) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_person_fkey FOREIGN KEY (person) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: tickets_priority_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_priority_fkey FOREIGN KEY (priority) REFERENCES priorities(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_reportby_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_reportby_fkey FOREIGN KEY (reportby) REFERENCES companypersons(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_status_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_status_fkey FOREIGN KEY (status) REFERENCES statuses(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: tickets_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY tickets
    ADD CONSTRAINT tickets_type_fkey FOREIGN KEY (type) REFERENCES eventtypes(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: timekeeping_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY timekeeping
    ADD CONSTRAINT timekeeping_person_fkey FOREIGN KEY (person) REFERENCES companypersons(id);


--
-- Name: xmpp_logs_project_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY xmpp_logs
    ADD CONSTRAINT xmpp_logs_project_fkey FOREIGN KEY (project) REFERENCES projects(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: xmpp_logs_ticket_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY xmpp_logs
    ADD CONSTRAINT xmpp_logs_ticket_fkey FOREIGN KEY (ticket) REFERENCES tickets(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

