-- phpMyAdmin SQL Dump
-- version 4.6.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jul 17, 2016 at 11:44 PM
-- Server version: 5.5.49-MariaDB
-- PHP Version: 5.6.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `comp437`
--

-- --------------------------------------------------------

--
-- Table structure for table `note`
--

CREATE TABLE `note` (
  `id` bigint(20) NOT NULL,
  `lastupdate` bigint(20) NOT NULL,
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` mediumtext COLLATE utf8_unicode_ci,
  `userid` bigint(20) NOT NULL,
  `isdeleted` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `note`
--

INSERT INTO `note` (`id`, `lastupdate`, `title`, `content`, `userid`, `isdeleted`) VALUES
(1, 1458980772721, '輸入', '輸入yeah', 1, 1),
(2, 1458980515868, '搞掂', '跳起yeah', 1, 0),
(3, 1458981829853, ' buying list', '三隻蝦', 1, 1),
(4, 1458981874836, '水果', '蘋果', 2, 0),
(5, 1460617403209, '中文', '心文kkk,竹竹', 3, 0),
(6, 1460617178354, '竹土', '弓一大,月竹土', 3, 0),
(7, 1460609869218, '哈', '土土', 3, 0),
(8, 1458982887005, 'haha', 'Hehe', 4, 0),
(9, 1458987386978, 'food', '薯片,可樂,啤酒,杯麵,香口膠,testing, haha', 1, 0),
(10, 1459015209662, 'Test note', 'Item1,Item2', 5, 0),
(11, 1459015238030, 'Test 2', 'Pk', 5, 0),
(12, 1459015224179, 'Test 3', '哈囉', 5, 0),
(13, 1460613935006, 'English note', 'Item1,New item', 6, 0),
(14, 1460615198915, 'Test', 'Item1', 6, 1),
(15, 1460617371101, 'hihi', '漢堡hi', 1, 0),
(16, 1460616918544, '14/4/2016 12:57', '', 3, 1),
(17, 1460616910430, '14/4/2016 14:47', 'Howwwwww,Irkk', 3, 1),
(18, 1460621803284, 'Buying list1', 'Sugar,Item2', 7, 0),
(19, 1460620406436, 'Test no internet access', 'Item1', 7, 1),
(20, 1460872250507, 'Today', 'Sunday', 8, 0),
(21, 1461315830384, '22/4/2016 16:56', '(empty)', 9, 1),
(22, 1461315989192, 'testing ', 'item 1', 9, 1),
(23, 1461315916785, 'Hhh', 'Ggg', 9, 1),
(24, 1461315978655, 'H f ', '', 9, 1),
(25, 1461315975001, 'fgvcv', 'cvbh', 9, 1),
(26, 1461316027012, '22/4/2016 17:05', '', 9, 1),
(27, 1461316131066, 'fbxxb', '', 9, 1),
(28, 1461316144496, '22/4/2016 17:06', '', 9, 1),
(29, 1461326106710, 'Buy list', '蘋果,香蕉,apple,洗衣粉,我後', 9, 0),
(30, 1461326039180, '我後d', '', 9, 1),
(31, 1461326035807, '我先', '', 9, 1),
(32, 1461396259018, 'dvv 1357', 'tdhg,l k ', 9, 0),
(33, 1461395981890, 'htedb', 'apple,蘋果', 9, 0),
(34, 1461396270879, 'hgfh竹竹', 'chj,竹竹', 9, 0);

-- --------------------------------------------------------

--
-- Table structure for table `noteuser`
--

CREATE TABLE `noteuser` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `pw` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `noteuser`
--

INSERT INTO `noteuser` (`id`, `name`, `pw`) VALUES
(1, 'argon', '123'),
(2, 'uuu', 'uuu'),
(3, 'tonytony', '123'),
(4, 'te', 'te'),
(5, 'Ben', '111'),
(6, 'Username1', 'test123'),
(7, 'Chantaiman', 'chantaiman'),
(8, 'tonyfok', 'tonytony'),
(9, 'Candy0715', '0715');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `note`
--
ALTER TABLE `note`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `noteuser`
--
ALTER TABLE `noteuser`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `note`
--
ALTER TABLE `note`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT for table `noteuser`
--
ALTER TABLE `noteuser`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
