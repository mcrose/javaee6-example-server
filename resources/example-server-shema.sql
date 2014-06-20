
CREATE SEQUENCE public.continents_id_seq;

CREATE TABLE public.continents (
                id BIGINT NOT NULL DEFAULT nextval('public.continents_id_seq'),
                name VARCHAR(50) NOT NULL,
                active BOOLEAN DEFAULT true,
                CONSTRAINT continents_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.continents_id_seq OWNED BY public.continents.id;

CREATE SEQUENCE public.countries_id_seq;

CREATE TABLE public.countries (
                id BIGINT NOT NULL DEFAULT nextval('public.countries_id_seq'),
                continent_id BIGINT NOT NULL,
                name VARCHAR(100) NOT NULL,
                active BOOLEAN DEFAULT true,
                CONSTRAINT countries_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.countries_id_seq OWNED BY public.countries.id;

CREATE SEQUENCE public.states_id_seq;

CREATE TABLE public.states (
                id BIGINT NOT NULL DEFAULT nextval('public.states_id_seq'),
                country_id BIGINT NOT NULL,
                name VARCHAR(100) NOT NULL,
                active BOOLEAN DEFAULT true,
                CONSTRAINT states_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN public.states.id IS 'for federates countries is state, for republics is department, we choose state because is shorter';


ALTER SEQUENCE public.states_id_seq OWNED BY public.states.id;

CREATE SEQUENCE public.cities_id_seq;

CREATE TABLE public.cities (
                id BIGINT NOT NULL DEFAULT nextval('public.cities_id_seq'),
                state_id BIGINT NOT NULL,
                name VARCHAR(150) NOT NULL,
                active BOOLEAN DEFAULT true,
                CONSTRAINT cities_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.cities_id_seq OWNED BY public.cities.id;

CREATE SEQUENCE public.neighborhoods_id_seq;

CREATE TABLE public.neighborhoods (
                id BIGINT NOT NULL DEFAULT nextval('public.neighborhoods_id_seq'),
                city_id BIGINT NOT NULL,
                name VARCHAR(150) NOT NULL,
                active BOOLEAN DEFAULT true,
                CONSTRAINT neighborhoods_pk PRIMARY KEY (id)
);


ALTER SEQUENCE public.neighborhoods_id_seq OWNED BY public.neighborhoods.id;

ALTER TABLE public.countries ADD CONSTRAINT continent_country_fk
FOREIGN KEY (continent_id)
REFERENCES public.continents (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.states ADD CONSTRAINT countries_states_fk
FOREIGN KEY (country_id)
REFERENCES public.countries (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.cities ADD CONSTRAINT states_cities_fk
FOREIGN KEY (state_id)
REFERENCES public.states (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.neighborhoods ADD CONSTRAINT cities_neighborhoods_fk
FOREIGN KEY (city_id)
REFERENCES public.cities (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
