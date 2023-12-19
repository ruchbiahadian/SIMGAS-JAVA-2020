-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 04, 2020 at 02:24 PM
-- Server version: 10.4.10-MariaDB
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_simgas`
--

-- --------------------------------------------------------

--
-- Table structure for table `t_detail_pelanggan`
--

CREATE TABLE `t_detail_pelanggan` (
  `kd_pelanggan` varchar(20) NOT NULL,
  `namap` varchar(35) NOT NULL,
  `keterangan` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `t_pangkalan`
--

CREATE TABLE `t_pangkalan` (
  `namaPang` varchar(35) NOT NULL,
  `alamatPang` varchar(50) NOT NULL,
  `telpPang` varchar(35) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `t_penjualan`
--

CREATE TABLE `t_penjualan` (
  `tanggal` varchar(20) NOT NULL,
  `kd_pelanggan` varchar(20) NOT NULL,
  `namap` varchar(35) NOT NULL,
  `asal` varchar(35) NOT NULL,
  `jenis` varchar(35) NOT NULL,
  `barang` varchar(35) NOT NULL,
  `keterangan` varchar(60) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `harga` varchar(35) NOT NULL,
  `harga_total` int(11) NOT NULL,
  `laba` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `t_perusahaan`
--

CREATE TABLE `t_perusahaan` (
  `tanggal` varchar(35) NOT NULL,
  `kd_barang` varchar(35) NOT NULL,
  `barang` varchar(50) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `harga` varchar(50) NOT NULL,
  `harga_total` int(11) NOT NULL,
  `status` varchar(35) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `t_detail_pelanggan`
--
ALTER TABLE `t_detail_pelanggan`
  ADD KEY `kd_pelanggan` (`kd_pelanggan`);

--
-- Indexes for table `t_penjualan`
--
ALTER TABLE `t_penjualan`
  ADD PRIMARY KEY (`kd_pelanggan`);

--
-- Indexes for table `t_perusahaan`
--
ALTER TABLE `t_perusahaan`
  ADD PRIMARY KEY (`kd_barang`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `t_detail_pelanggan`
--
ALTER TABLE `t_detail_pelanggan`
  ADD CONSTRAINT `t_detail_pelanggan_ibfk_1` FOREIGN KEY (`kd_pelanggan`) REFERENCES `t_penjualan` (`kd_pelanggan`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
