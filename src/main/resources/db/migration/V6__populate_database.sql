INSERT INTO categories (name)
VALUES ('Produce'),
       ('Dairy'),
       ('Bakery'),
       ('Meat & Seafood'),
       ('Pantry'),
       ('Beverages'),
       ('Snacks');

INSERT INTO products (name, price, description, category_id)
VALUES
-- Produce
('Organic Bananas (1 lb)',
 0.69,
 'Fresh organic bananas, perfect for snacking, smoothies, or baking.',
 1),

('Gala Apples (1 lb)',
 1.49,
 'Crisp and sweet Gala apples, great for eating fresh or making pies.',
 1),

-- Dairy
('Whole Milk – 1 Gallon',
 3.79,
 'Grade A whole milk from grass-fed cows, rich and creamy.',
 2),

('Cage-Free Large Eggs (12 ct)',
 4.29,
 'A dozen cage-free large brown eggs, ideal for breakfast or baking.',
 2),

-- Bakery
('Artisan Sourdough Bread',
 4.99,
 'Freshly baked sourdough loaf with a crispy crust and soft interior.',
 3),

-- Meat & Seafood
('Boneless Skinless Chicken Breast (1 lb)',
 6.49,
 'Fresh boneless, skinless chicken breasts, high in protein and low in fat.',
 4),

('Atlantic Salmon Fillet (1 lb)',
 11.99,
 'Fresh Atlantic salmon fillet, rich in omega-3 fatty acids.',
 4),

-- Pantry
('Barilla Spaghetti Pasta (16 oz)',
 1.89,
 'Classic Italian spaghetti made from high-quality durum wheat.',
 5),

('Extra Virgin Olive Oil (500 ml)',
 8.99,
 'Cold-pressed extra virgin olive oil, perfect for cooking and salads.',
 5),

-- Beverages
('Coca-Cola Classic (12 oz can)',
 1.25,
 'Refreshing Coca-Cola classic with original flavor.',
 6),

('Orange Juice – No Pulp (52 oz)',
 4.59,
 '100% pure orange juice with no added sugar and no pulp.',
 6),

-- Snacks
('Lay’s Classic Potato Chips (8 oz)',
 4.49,
 'Crispy classic potato chips made with simple ingredients.',
 7),

('Kind Dark Chocolate Nuts & Sea Salt Bar',
 2.29,
 'Nut-based snack bar with dark chocolate and a touch of sea salt.',
 7);
