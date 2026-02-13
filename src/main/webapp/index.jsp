<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta charset="UTF-8">
    <title>Eğlenceli HTML/CSS</title>
    <style>
        /* Arka plan animasyonu: Renkler arasında yumuşak geçiş */
        body {
            margin: 0;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
            background-size: 400% 400%;
            animation: gradientBG 10s ease infinite;
            overflow: hidden;
        }

        @keyframes gradientBG {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        /* Ana Kart */
        .card {
            background: rgba(255, 255, 255, 0.2);
            padding: 50px;
            border-radius: 20px;
            backdrop-filter: blur(10px);
            border: 2px solid rgba(255, 255, 255, 0.3);
            text-align: center;
            color: white;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            transition: all 0.3s ease;
            cursor: pointer;
            /* Sürekli hafifçe zıplama animasyonu */
            animation: bounce 2s ease-in-out infinite;
        }

        /* Üzerine gelince büyüme ve parlama */
        .card:hover {
            transform: scale(1.1) rotate(5deg);
            background: rgba(255, 255, 255, 0.3);
            box-shadow: 0 20px 50px rgba(0,0,0,0.3);
        }

        h1 {
            font-size: 3rem;
            margin: 0;
            animation: textGlow 2s ease-in-out infinite alternate;
        }

        p {
            font-size: 1.2rem;
            opacity: 0.9;
        }

        /* Zıplama efekti */
        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-20px); }
        }

        /* Yazı parlama efekti */
        @keyframes textGlow {
            from { text-shadow: 0 0 10px #fff, 0 0 20px #fff; }
            to { text-shadow: 0 0 20px #ff00de, 0 0 30px #ff00de; }
        }

        /* Süs amaçlı küçük daireler (CSS ile) */
        .decor {
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.1);
            animation: float 15s infinite linear;
            z-index: -1;
        }

        @keyframes float {
            from { transform: translateY(100vh) rotate(0deg); }
            to { transform: translateY(-10vh) rotate(360deg); }
        }
    </style>
</head>
<body>

    <div class="decor" style="width: 80px; height: 80px; left: 10%; animation-duration: 20s;"></div>
    <div class="decor" style="width: 120px; height: 120px; left: 80%; animation-duration: 15s;"></div>
    <div class="decor" style="width: 50px; height: 50px; left: 40%; animation-duration: 25s;"></div>

    <div class="card">
        <h1>Hello World! ✨</h1>
        <p>JavaScript yok, sadece saf CSS büyüsü var.</p>
        <p><i>(Üzerime gelmeyi dene!)</i></p>
    </div>

</body>
</html>