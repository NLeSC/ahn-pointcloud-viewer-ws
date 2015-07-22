CREATE EXTENSION postgis;

CREATE TABLE extent_raw (filepath text, numberpoints integer, geom public.geometry(Geometry, 28992));
CREATE TABLE extent_potree (filepath text, level integer, numberpoints integer, geom public.geometry(Geometry, 28992));
CREATE TABLE potree_dist (level integer, numberfiles integer, numberpoints integer, ratio float);

CREATE INDEX extent_raw_geom_idx ON extent_raw USING GIST (geom);
CREATE INDEX extent_potree_geom_idx ON extent_potree USING GIST (geom);

INSERT INTO extent_raw (filepath, numberpoints, geom) VALUES ('25gn2_11.laz', 15189137, st_geomFromText('POLYGON((126000 483750,125000 483750,125000 485000,126000 485000,126000 483750))', 28992));
INSERT INTO extent_raw (filepath, numberpoints, geom) VALUES ('25gn2_16.laz', 13245300, st_geomFromText('POLYGON((126000 482500,125000 482500,125000 483750,126000 483750,126000 482500))', 28992));
INSERT INTO extent_potree (filepath, level, numberpoints, geom) VALUES ('r634853426428.laz', 12, 2797284, st_geomFromText('POLYGON((126000 482750,125000 482750,125000 485000,126000 485000,126000 482750))', 28992));
INSERT INTO extent_potree (filepath, level, numberpoints, geom) VALUES ('r634853426421.laz', 12, 3213253, st_geomFromText('POLYGON((125500 482500,125000 482500,125000 485000,125500 485000,125500 482500))', 28992));
INSERT INTO extent_potree (filepath, level, numberpoints, geom) VALUES ('r634853426422.laz', 12, 2245300, st_geomFromText('POLYGON((126000 482500,125000 482500,125000 482500,126000 483250,126000 482500))', 28992));
INSERT INTO extent_potree (filepath, level, numberpoints, geom) VALUES ('r634853426423.laz', 12, 2311325, st_geomFromText('POLYGON((125500 482500,125000 482500,125000 482500,125500 483250,125500 482500))', 28992));

INSERT INTO potree_dist (ratio, level) VALUES (0.7, 12);




