

Cambiarle el nombre a los endpoints para que esten acorde a el proyecto.    
Tira un error en un bean de Hateaos en productController, resuelto.
igualmente consultar por tercer parametro null en assembler.




//Crear en base de datos esto.  

-- Evitar nombres duplicados de productos (ignorando mayúsculas)
CREATE UNIQUE INDEX ux_products_name_lower ON products (LOWER(name));

-- Evitar nombres duplicados de categorías
CREATE UNIQUE INDEX ux_categories_name_lower ON categories (LOWER(name));

-- Evitar nombres de razón social duplicados
CREATE UNIQUE INDEX ux_providers_business_name_lower ON providers (LOWER(business_name));

-- Evitar emails duplicados
CREATE UNIQUE INDEX ux_providers_email_lower ON providers (LOWER(email));



