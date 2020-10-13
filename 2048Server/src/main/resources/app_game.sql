--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: play_2048; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.play_2048 (
    user_id bigint NOT NULL,
    score integer DEFAULT 0 NOT NULL,
    play_time date NOT NULL,
    final_result text
);


ALTER TABLE public.play_2048 OWNER TO postgres;

--
-- Name: play_board; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.play_board (
    user_id bigint NOT NULL,
    take_time integer DEFAULT 0 NOT NULL,
    play_time date NOT NULL,
    initial_board text
);


ALTER TABLE public.play_board OWNER TO postgres;

--
-- Name: play_score; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.play_score (
    user_id bigint NOT NULL,
    max_score integer
);


ALTER TABLE public.play_score OWNER TO postgres;

--
-- Name: play_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.play_user (
    user_id bigint NOT NULL,
    user_password character varying(50) NOT NULL,
    user_name character varying(30) NOT NULL,
    user_gender character varying(6) NOT NULL
);


ALTER TABLE public.play_user OWNER TO postgres;

--
-- Data for Name: play_2048; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.play_2048 (user_id, score, play_time, final_result) FROM stdin;
123456	1024	2020-06-13	
123456	2048	2020-06-13	
123456	5096	2020-06-13	
123456	2346	2020-06-13	''
18373796017	302	2020-06-13	[ [2 4 8 4 ]  [4 64 4 2 ]  [16 128 16 4 ]  [32 8 4 2 ] ]
\.


--
-- Data for Name: play_board; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.play_board (user_id, take_time, play_time, initial_board) FROM stdin;
\.


--
-- Data for Name: play_score; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.play_score (user_id, max_score) FROM stdin;
\.


--
-- Data for Name: play_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.play_user (user_id, user_password, user_name, user_gender) FROM stdin;
1346790	1354975	zyf	man
135468465	246810	ldw	Man
123456	123456	lxh	Man
18373796017	123456	LiuXianghai	Man
\.


--
-- Name: play_2048 play_2048_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play_2048
    ADD CONSTRAINT play_2048_pkey PRIMARY KEY (user_id, score, play_time);


--
-- Name: play_board play_board_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play_board
    ADD CONSTRAINT play_board_pkey PRIMARY KEY (user_id, take_time, play_time);


--
-- Name: play_score play_score_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play_score
    ADD CONSTRAINT play_score_pkey PRIMARY KEY (user_id);


--
-- Name: play_user play_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play_user
    ADD CONSTRAINT play_user_pkey PRIMARY KEY (user_id);


--
-- Name: play_2048 play_2048_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play_2048
    ADD CONSTRAINT play_2048_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.play_user(user_id);


--
-- Name: play_board play_board_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.play_board
    ADD CONSTRAINT play_board_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.play_user(user_id);


--
-- PostgreSQL database dump complete
--

